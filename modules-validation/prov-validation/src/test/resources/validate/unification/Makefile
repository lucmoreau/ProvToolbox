
go:
	$(MAKE) NAME=fail1 do-end
	$(MAKE) NAME=fail2 do-end
	$(MAKE) NAME=fail3 do-end
	$(MAKE) NAME=fail4 do-end
	$(MAKE) NAME=fail5 do-end
	$(MAKE) NAME=fail5 do-end
	$(MAKE) NAME=success1 do-end
	$(MAKE) NAME=success2 do-end
	$(MAKE) NAME=success3 do-end
	$(MAKE) NAME=success4 do-end
	$(MAKE) NAME=success5 do-end
	$(MAKE) NAME=success6 do-end
#
	$(MAKE) NAME=fail1 do-invalidate
	$(MAKE) NAME=fail2 do-invalidate
	$(MAKE) NAME=fail3 do-invalidate
	$(MAKE) NAME=fail4 do-invalidate
	$(MAKE) NAME=fail5 do-invalidate
	$(MAKE) NAME=fail6 do-invalidate
	$(MAKE) NAME=fail7 do-invalidate
	$(MAKE) NAME=success1 do-invalidate
	$(MAKE) NAME=success2 do-invalidate
	$(MAKE) NAME=success3 do-invalidate
	$(MAKE) NAME=success4 do-invalidate
	$(MAKE) NAME=success5 do-invalidate
	$(MAKE) NAME=success6 do-invalidate
	$(MAKE) NAME=success7 do-invalidate
	$(MAKE) NAME=success8 do-invalidate
#
	$(MAKE) NAME=fail1 do-usage
	$(MAKE) NAME=fail2 do-usage
	$(MAKE) NAME=fail3 do-usage
	$(MAKE) NAME=fail4 do-usage
	$(MAKE) NAME=fail5 do-usage
	$(MAKE) NAME=fail6 do-usage
	$(MAKE) NAME=fail7 do-usage
	$(MAKE) NAME=success1 do-usage
	$(MAKE) NAME=success2 do-usage
	$(MAKE) NAME=success3 do-usage
	$(MAKE) NAME=success4 do-usage
	$(MAKE) NAME=success5 do-usage
	$(MAKE) NAME=success6 do-usage
	$(MAKE) NAME=success7 do-usage
	$(MAKE) NAME=success8 do-usage
#
	$(MAKE) NAME=fail1 do-attribution
	$(MAKE) NAME=fail2 do-attribution
	$(MAKE) NAME=success1 do-attribution
	$(MAKE) NAME=success2 do-attribution
	$(MAKE) NAME=success3 do-attribution
	$(MAKE) NAME=success4 do-attribution
#
	$(MAKE) NAME=fail1 do-delegation
	$(MAKE) NAME=fail2 do-delegation
	$(MAKE) NAME=fail3 do-delegation
	$(MAKE) NAME=fail4 do-delegation
	$(MAKE) NAME=fail5 do-delegation
	$(MAKE) NAME=fail6 do-delegation
	$(MAKE) NAME=success1 do-delegation
	$(MAKE) NAME=success2 do-delegation
	$(MAKE) NAME=success3 do-delegation
	$(MAKE) NAME=success4 do-delegation
	$(MAKE) NAME=success5 do-delegation
#
	$(MAKE) NAME=fail1 do-influence
	$(MAKE) NAME=fail2 do-influence
	$(MAKE) NAME=success1 do-influence
	$(MAKE) NAME=success2 do-influence
	$(MAKE) NAME=success3 do-influence
	$(MAKE) NAME=success4 do-influence



do-end:
	sed -e s/start/end/ start-$(NAME).org.openprovenance.prov.xml | sed -e s/Start/End/ > end-$(NAME).org.openprovenance.prov.xml

do-invalidate:
	sed -e s/generate/invalidate/ generation-$(NAME).org.openprovenance.prov.xml | sed -e s/Generate/Invalidate/ > invalidation-$(NAME).org.openprovenance.prov.xml

do-usage:
	sed -e s/wasGeneratedBy/used/ generation-$(NAME).org.openprovenance.prov.xml | sed -e s/gen/use/ | sed -e s/entity/XXX/ | sed -e s/activity/entity/ | sed -e s/XXX/activity/  > usage-$(NAME).org.openprovenance.prov.xml

do-attribution:
	sed -e s/wasInformedBy/wasAttributedTo/ communication-$(NAME).org.openprovenance.prov.xml | sed -e s/inf/del/ | sed -e 's/prov:activity prov:id="ex:a1"><\/prov:activity/prov:entity prov:id="ex:a1"><\/prov:entity/' | sed -e s/activity/agent/ | sed -e s/effect/entity/  | sed -e s/ex:a1/ex:e1/  | sed -e s/ex:a2/ex:ag2/ | sed -e s/cause/agent/  > attribution-$(NAME).org.openprovenance.prov.xml

do-delegation:
	sed -e s/wasAssociatedWith/actedOnBehalfOf/ association-$(NAME).org.openprovenance.prov.xml | sed -e s/inf/del/ | sed -e 's/activity prov:ref/subordinate prov:ref/' | sed -e 's/agent prov:ref/responsible prov:ref/' | sed -e s/activity/agent/ | sed -e s/ex:a1/ex:ag2/ | sed -e s/plan/activity/ | sed -e s/entity/activity/ | sed -e s/effect/subordinate/ | sed -e s/assoc/del/ |  sed -e s/ex:e1/ex:a2/ | sed -e s/cause/responsible/ | sed -e 's/<prov:type.*prov:type>//' > delegation-$(NAME).org.openprovenance.prov.xml


do-influence:
	sed -e s/wasInformedBy/wasInfluencedBy/ communication-$(NAME).org.openprovenance.prov.xml | sed -e s/inf/del/ | sed -e s/effect/influencee/  | sed -e s/ex:a1/ex:x1/  | sed -e s/ex:a2/ex:x2/ | sed -e s/cause/influencer/| sed -e s/del/infl/ | sed -e 's/<prov:activity.*>//'  | sed -e 's/<\/prov:activity>//'  > influence-$(NAME).org.openprovenance.prov.xml

go3:
	$(MAKE) NAME=fail1 do-end
	sed -e s/start/end/ start-fail1.org.openprovenance.prov.xml | sed -e s/Start/End/ > end-fail1.org.openprovenance.prov.xml
	sed -e s/start/end/ start-fail2.org.openprovenance.prov.xml | sed -e s/Start/End/ > end-fail2.org.openprovenance.prov.xml
	sed -e s/start/end/ start-fail3.org.openprovenance.prov.xml | sed -e s/Start/End/ > end-fail3.org.openprovenance.prov.xml
	sed -e s/start/end/ start-fail4.org.openprovenance.prov.xml | sed -e s/Start/End/ > end-fail4.org.openprovenance.prov.xml
	sed -e s/start/end/ start-success1.org.openprovenance.prov.xml  | sed -e s/Start/End/ > end-success1.org.openprovenance.prov.xml
	sed -e s/start/end/ start-success2.org.openprovenance.prov.xml  | sed -e s/Start/End/ > end-success2.org.openprovenance.prov.xml
	sed -e s/start/end/ start-success3.org.openprovenance.prov.xml  | sed -e s/Start/End/ > end-success3.org.openprovenance.prov.xml
	sed -e s/start/end/ start-success4.org.openprovenance.prov.xml  | sed -e s/Start/End/ > end-success4.org.openprovenance.prov.xml
	sed -e s/start/end/ start-success5.org.openprovenance.prov.xml  | sed -e s/Start/End/ > end-success5.org.openprovenance.prov.xml
	sed -e s/start/end/ start-success6.org.openprovenance.prov.xml  | sed -e s/Start/End/ > end-success6.org.openprovenance.prov.xml


%.provn: %.org.openprovenance.prov.xml
	provconvert -infile $< -outfile $@


luc:
	$(MAKE) activity-end-fail1.provn activity-end-success1.provn activity-start-fail1.provn activity-start-success1.provn activity-success1.provn activity-success2.provn activity-success3.provn association-success3.provn association-success4.provn association-success5.provn attributes-activity-success1.provn attributes-end-success1.provn attributes-end-success2.provn attributes-entity-success1.provn bundle-success1.provn bundle-success2.provn communication-fail1.provn communication-fail2.provn communication-success1.provn communication-success2.provn communication-success3.provn delegation-success3.provn delegation-success4.provn delegation-success5.provn derivation-fail1.provn derivation-fail2.provn derivation-fail3.provn derivation-fail4.provn end-fail5.provn end-success1.provn end-success2.provn end-success3.provn end-success4.provn end-success5.provn end-success6.provn generation-success3.provn generation-success4.provn generation-success5.provn generation-success6.provn generation-success7.provn generation-success8.provn influence-fail1.provn invalidation-fail5.provn invalidation-fail6.provn invalidation-fail7.provn invalidation-success1.provn invalidation-success2.provn invalidation-success3.provn invalidation-success4.provn mention-fail4.provn mention-success1.provn mention-success2.provn specialization-fail1.provn specialization-fail2.provn specialization-fail3.provn specialization-fail4.provn start-fail8.provn start-success1.provn start-success2.provn start-success3.provn start-success4.provn start-success5.provn start-success6.provn usage-fail7.provn usage-success1.provn usage-success2.provn usage-success3.provn usage-success4.provn usage-success5.provn activity-success4.provn association-fail1.provn association-fail2.provn association-fail3.provn association-fail4.provn association-fail5.provn association-fail6.provn association-success1.provn association-success2.provn attributes-start-success1.provn attributes-start-success2.provn attribution-fail1.provn attribution-fail2.provn attribution-success1.provn attribution-success2.provn attribution-success3.provn attribution-success4.provn bundle-fail1.provn communication-success4.provn delegation-fail1.provn delegation-fail2.provn delegation-fail3.provn delegation-fail4.provn delegation-fail5.provn delegation-fail6.provn delegation-success1.provn delegation-success2.provn derivation-success1.provn derivation-success2.provn derivation-success3.provn derivation-success4.provn derivation-success5.provn end-fail1.provn end-fail2.provn end-fail3.provn end-fail4.provn generation-fail1.provn generation-fail2.provn generation-fail3.provn generation-fail4.provn generation-fail5.provn generation-fail6.provn generation-fail7.provn generation-success1.provn generation-success2.provn influence-fail2.provn influence-success1.provn influence-success2.provn influence-success3.provn influence-success4.provn invalidation-fail1.provn invalidation-fail2.provn invalidation-fail3.provn invalidation-fail4.provn invalidation-success5.provn invalidation-success6.provn invalidation-success7.provn invalidation-success8.provn membership-fail1.provn membership-success1.provn mention-fail1.provn mention-fail2.provn mention-fail3.provn specialization-success1.provn specialization-success2.provn start-fail1.provn start-fail2.provn start-fail3.provn start-fail4.provn start-fail5.provn start-fail6.provn start-fail7.provn start-success7.provn start-success8.provn start-success9.provn usage-fail1.provn usage-fail2.provn usage-fail3.provn usage-fail4.provn usage-fail5.provn usage-fail6.provn usage-success6.provn usage-success7.provn usage-success8.provn
