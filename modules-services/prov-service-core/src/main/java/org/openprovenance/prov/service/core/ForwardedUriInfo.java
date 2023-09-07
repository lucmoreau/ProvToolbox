package org.openprovenance.prov.service.core;

/**
 * Copyright (c) 2011-2012, ReXSL.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the ReXSL.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.PathSegment;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Custom implementation of {@link UriInfo} that is aware of
 * {@code X-Forwarded-For} HTTP header.
 *
 * <p>The class is mutable and NOT thread-safe.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id: ForwardedUriInfo.java 2226 2012-11-13 08:27:20Z guard $
 * @see <a href="http://tools.ietf.org/html/draft-ietf-appsawg-http-forwarded-10">IETF Forwarded HTTP Extension</a>
 */
final public class ForwardedUriInfo implements UriInfo {
    static Logger logger = LogManager.getLogger(ForwardedUriInfo.class);

    /**
     * Original {@link UriInfo}.
     */
    private final transient UriInfo info;

    /**
     * Http headers, injected by JAX-RS implementation.
     */
    private final transient AtomicReference<HttpHeaders> headers;

    /**
     * TRUE if HTTP headers already where analyzed.
     */
    private transient boolean analyzed;

    /**
     * New host, or empty string if not required, or NULL if not yet sure.
     */
    private transient String host;

    /**
     * Scheme to set, or NULL if not necessary.
     */
    private transient String scheme;

    /**
     * Public ctor.
     * @param inf The original UriInfo
     * @param hdrs HTTP headers
     */
    public ForwardedUriInfo(final UriInfo inf,
        final AtomicReference<HttpHeaders> hdrs) {
        if (inf == null) {
            throw new IllegalStateException(
                "UriInfo is incorrectly injected into BaseResource"
            );
        }
        this.info = inf;
        this.headers = hdrs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URI getAbsolutePath() {
        return this.getAbsolutePathBuilder().build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UriBuilder getAbsolutePathBuilder() {
        return this.forward(this.info.getAbsolutePathBuilder());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URI getBaseUri() {
        return this.getBaseUriBuilder().build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UriBuilder getBaseUriBuilder() {
        return this.forward(this.info.getBaseUriBuilder());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URI getRequestUri() {
        return this.getRequestUriBuilder().build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UriBuilder getRequestUriBuilder() {
        return this.forward(this.info.getRequestUriBuilder());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Object> getMatchedResources() {
        return this.info.getMatchedResources();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getMatchedURIs() {
        return this.info.getMatchedURIs();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getMatchedURIs(final boolean decode) {
        return this.info.getMatchedURIs(decode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPath() {
        return this.info.getPath();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPath(final boolean decode) {
        return this.info.getPath(decode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MultivaluedMap<String, String> getPathParameters() {
        return this.info.getPathParameters();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MultivaluedMap<String, String> getPathParameters(
        final boolean decode) {
        return this.info.getPathParameters(decode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PathSegment> getPathSegments() {
        return this.info.getPathSegments();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PathSegment> getPathSegments(final boolean decode) {
        return this.info.getPathSegments(decode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MultivaluedMap<String, String> getQueryParameters() {
        return this.info.getQueryParameters();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MultivaluedMap<String, String> getQueryParameters(
        final boolean decode) {
        return this.info.getQueryParameters(decode);
    }

    /**
     * Forward this builder to the right host/port (if necessary).
     * @param builder The builder to forward
     * @return The same builder
     */
    private UriBuilder forward(final UriBuilder builder) {
        if (!this.analyzed) {
            if (this.headers.get() == null) {
                throw new IllegalStateException(
                    "HttpHeaders is not injected into BaseResource"
                );
            }
            for (Map.Entry<String, List<String>> header
                : this.headers.get().getRequestHeaders().entrySet()) {
                for (String value : header.getValue()) {
                    this.consume(header.getKey(), value);
                }
            }
            logger.debug("#forward(..): analyzed, host=" + this.host + ", scheme=" + this.scheme);

            this.analyzed = true;
        }
        if (this.host != null) {
            builder.host(this.host);
            int ndx=this.host.indexOf(":");
            if (ndx==-1) {
                logger.debug("unsetting port");
                builder.port(-1);
            } else {
                int myport=Integer.parseInt(this.host.substring(ndx+1));
                logger.debug("setting port to " + myport);

                builder.port(myport);
            }
            
        }
        if (this.scheme != null) {
            builder.scheme(this.scheme);
        }
        return builder;
    }

    /**
     * Interpret HTTP header and save host/scheme pair into this object.
     * @param name HTTP header name
     * @param value HTTP header value
     * @see <a href="http://tools.ietf.org/html/draft-ietf-appsawg-http-forwarded-10">IETF Forwarded HTTP Extension</a>
     */
    private void consume(final String name, final String value) {
        if (this.host == null
            && "x-forwarded-host".equals(name.toLowerCase(Locale.ENGLISH))) {
            this.host = value;
        } else if (this.scheme == null
            && "x-forwarded-proto".equals(name.toLowerCase(Locale.ENGLISH))) {
            this.scheme = value;
        } else if ("forwarded".equals(name.toLowerCase(Locale.ENGLISH))) {
            this.forwarded(value);
        }
    }

    /**
     * Consume specifically "Forwarded" header.
     * @param value HTTP header value
     */
    private void forwarded(final String value) {
        for (String sector : value.split("\\s*,\\s*")) {
            for (String opt : sector.split("\\s*;\\s*")) {
                final String[] parts = opt.split("=", 2);
                if (this.host == null && "host".equals(parts[0])) {
                    this.host = parts[1];
                }
                if (this.scheme == null && "proto".equals(parts[0])) {
                    this.scheme = parts[1];
                }
            }
        }
    }

    @Override
    public URI resolve(URI uri) {
	return info.resolve(uri);
    }

    @Override
    public URI relativize(URI uri) {
	return info.relativize(uri);
    }

}