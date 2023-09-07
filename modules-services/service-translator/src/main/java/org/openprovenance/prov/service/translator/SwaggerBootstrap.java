/**
 *  Copyright 2012 Wordnik, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openprovenance.prov.service.translator;


import jakarta.servlet.http.HttpServlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;



//@WebServlet(name="swaggerlet",urlPatterns={"/"})

abstract public class SwaggerBootstrap extends HttpServlet {
    /*
    Info info = new Info()
            .title("PROV API")
            .description("This is a PROV API server ")
            .termsOfService("https://openprovenance.org/ethics/provenance-web-services/")
            .contact(new Contact()
                    .email("provenance@kcl.ac.uk"))
          //  .license(new License()
          //          .name("Apache 2.0")
          //          .url("http://www.apache.org/licenses/LICENSE-2.0.html"))
            ;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        ServletContext context = config.getServletContext();
        Swagger swagger = new Swagger().info(info);
        

        swagger.tag(new Tag()
                    .name("provapi")
                    .description("Methods for provapi")
                    // .externalDocs(new ExternalDocs("Find out more", "http://swagger.io"))
                );


        swagger.tag(new Tag()
                .name("ptm")
                .description("Methods for ptm")
              //  .externalDocs(new ExternalDocs("Find out more", "http://swagger.io"))
                );
     

        swagger.tag(new Tag()
                .name("view")
                .description("Methods for view")
              //  .externalDocs(new ExternalDocs("Find out more", "http://swagger.io"))
                );


	    context.setAttribute("swagger", swagger);	

            System.out.println("+++++++++++++++++++ SwaggerBootstrap: ");
    }
    */
}
