package org.openprovenance.prov.service;

import org.openprovenance.prov.rules.Rules;
import org.openprovenance.prov.rules.RulesFactory;
import org.openprovenance.prov.service.core.PostService;
import org.openprovenance.prov.service.translation.TranslationService;

public class MetricsService extends TranslationService {

    public MetricsService(PostService ps) {
        super(ps);
        setRulesFactory(MetricsCalculator::new);
    }



}
