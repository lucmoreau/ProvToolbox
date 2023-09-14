package org.openprovenance.prov.scala.template

import java.util.{Hashtable, UUID}

import javax.xml.datatype.XMLGregorianCalendar
import org.openprovenance.prov.model.{Namespace, ProvUtilities, QualifiedNameUtils, StatementAction}
import org.openprovenance.prov.scala.immutable._
import org.openprovenance.prov.template._

import scala.collection.JavaConversions._


object TermExpander {
import ProvFactory.pf
import org.openprovenance.prov.template.ExpandUtil
	val UUID_PREFIX = "uuid"
  val URN_UUID_NS = "urn:uuid:"
  val qnU = new QualifiedNameUtils()
	  
  def getUUIDQualifiedName():QualifiedName = {
    val uuid = UUID.randomUUID();
    new QualifiedName(UUID_PREFIX,
    		              qnU.escapeProvLocalName(uuid.toString()),
    		              URN_UUID_NS)
  }
  val pu: ProvUtilities = new ProvUtilities()
  
  val qn_time     =pf.newQualifiedName(ExpandUtil.TMPL_NS, ExpandUtil.TIME,      ExpandUtil.TMPL_PREFIX).asInstanceOf[QualifiedName]
  val qn_startTime=pf.newQualifiedName(ExpandUtil.TMPL_NS, ExpandUtil.STARTTIME, ExpandUtil.TMPL_PREFIX).asInstanceOf[QualifiedName]
  val qn_endTime  =pf.newQualifiedName(ExpandUtil.TMPL_NS, ExpandUtil.ENDTIME,   ExpandUtil.TMPL_PREFIX).asInstanceOf[QualifiedName]
  val qn_label    =pf.newQualifiedName(ExpandUtil.TMPL_NS, ExpandUtil.LABEL,     ExpandUtil.TMPL_PREFIX).asInstanceOf[QualifiedName]
  
  val reservedNames=Map(qn_time->0,qn_startTime->1,qn_endTime->2,qn_label->3)

}

class TermExpander (allUpdatedRequired: Boolean, addOrderp: Boolean, index: java.util.List[Integer]) {
  import ProvFactory.pf
  import TermExpander._
  
  def expandAttributes(s:Statement, env: Hashtable[QualifiedName, java.util.List[TypedValue]]): (Boolean,Set[Attribute],TemplateAttributeValues) = {
    s match {
      case s: HasAttributes => { val ta=TemplateAttributeValues(None,None,None,None)
                            val expansions=s.getAttributes.map { attr =>  expandAttribute(attr,env,s,ta) }
        
                            val attrs:Set[Attribute]=expansions.flatMap(_._2)
                              
                            val bool=expansions.map(_._1).foldLeft(true){(b1,b2)=> b1&& b2}
                            (bool,attrs,ta)
      }
      case _ => (false, Set(),TemplateAttributeValues(None,None,None,None))
    }
  }
  
  case class TemplateAttributeValues(var time: Option[XMLGregorianCalendar],var start: Option[XMLGregorianCalendar],var end: Option[XMLGregorianCalendar],var label: Option[Label]) {
    def merge(ta:TemplateAttributeValues) = {
      TemplateAttributeValues (time  match { case Some(v) => Some(v) case _ => ta.time},
                               start match { case Some(v) => Some(v) case _ => ta.start},
                               end   match { case Some(v) => Some(v) case _ => ta.end},
                               label match { case Some(v) => Some(v) case _ => ta.label})
    }
    def update(ta:TemplateAttributeValues) {
      time  match { case Some(v) => time=Some(v) case _ => }
      start match { case Some(v) => start=Some(v) case _ => }
      end   match { case Some(v) => end=Some(v) case _ => }
      label match { case Some(v) => label=Some(v) case _ => }
    }    
  }
  
  
  def processTemplateAttributes(attribute:Attribute, env2: Hashtable[QualifiedName, java.util.List[TypedValue]], s:Statement, ta:TemplateAttributeValues): Boolean = {
    import scala.collection.JavaConversions._
  
        
    @inline def updateTime(t: Option[XMLGregorianCalendar]) {
        ta.time=t
    }
    @inline def updateStartTime(t: Option[XMLGregorianCalendar]){
        ta.start=t
    }
    @inline def updateEndTime(t: Option[XMLGregorianCalendar]){
        ta.end=t
    }
    @inline def updateLabel(t: Option[Label]) {
        ta.label=t
    }
        
    val reserved=(reservedNames.get(attribute.elementName))
    
    val res= reserved match {
      case None => false
      case Some(i) => {
        attribute.value match {
          case qn:QualifiedName => {
              //println("bingo attribute is QN " + attribute)

              val vals=env2.get(qn)
              val attr=if (vals==null) None else Some(vals.toList)
        
              attr match {
                 case None => 
                 case Some(set) => 
                     set.headOption match {
                        case None => 
                        case Some(v) =>
                          i match {
                             case 0 => updateTime     (Some (pf.newISOTime(v.value.toString)))
                             case 1 => updateStartTime(Some (pf.newISOTime(v.value.toString)))
                             case 2 => updateEndTime  (Some (pf.newISOTime(v.value.toString)))
                             case 3 => updateLabel    (Some (new Label(pf.xsd_string,new LangString(v.value.toString, None))))
                          }
                     }
              }
              
              true
          }
          case _ => false
        }
      }
    }

     res
      
  }
      

  

  def doExpand(id: QualifiedName, env: Hashtable[QualifiedName, QualifiedName], bindings:Bindings) = {
        if (ExpandUtil.isVariable(id)) {
            val v:QualifiedName  = env.get(id);
            if (v != null) {
               (true, v)
            } else {
                if (ExpandUtil.isGensymVariable(id)) {
                    val uuid:QualifiedName = getUUIDQualifiedName();
                    bindings.addVariable(id, uuid);
                    (true, uuid)
                } else {
                    (false, id)  
                }
            }
        } else {
            // this is not a variable, so it's regarded as expanded (in the sense that it doesn't contain an unexpanded variable)
            (true, id) 
        }
    }

  val null_attr_expansion: (Boolean,Set[Attribute])=(true, Set())
  
  def expandAttribute(attribute:Attribute, env2: Hashtable[QualifiedName, java.util.List[TypedValue]], s: Statement, ta:TemplateAttributeValues): (Boolean,Set[Attribute]) = {
    if (attribute.`type`== ProvFactory.pf.prov_qualified_name) {
    	val o=attribute.value
    	o match {
    	  case qn1:QualifiedName => {
    	    // if attribute is
    		  // constructed properly,
    		  // this test should always
    		  // return true
    	     if (ExpandUtil.isVariable(qn1)) { 
    	       val vals=env2.get(qn1);
    	       if (vals==null) {
    	           if (ExpandUtil.isGensymVariable(qn1)) {  
    	        	     (false, Set (pf.newAttribute(attribute.elementName,
    	        	    		                          getUUIDQualifiedName(),
    	        	    		                          pf.prov_qualified_name).asInstanceOf[Attribute]))
    	           } else {
    	             null_attr_expansion
    	           }
    	       } else {
    	         //println("expanding " + attribute)
    	         // TODO: see ExpandAction, delegeta to function to process time/label attrbiutes
    	         if (processTemplateAttributes(attribute, env2, s, ta)) {
    	           null_attr_expansion 
    	         } else {
    	          (true, vals.toSet.map{v:TypedValue =>
    	                               pf.newAttribute(attribute.elementName,
    	                                               v.getValue(),
    	                                               v.getType()).asInstanceOf[Attribute]})
    	         }
    	       }
    	       
    	     } else {
    	       (true, Set(attribute))
    	     }
    	  }
    	  case _ => (true,Set(attribute))
    	}
    } else {
      (true,Set(attribute))
    }
  }
  
  var allExpanded: Boolean=true

  
  def addOrderAttribute(attrs:Set[Attribute]) = {
        if (addOrderp) {
            attrs + pf.newAttribute(ExpandUtil.TMPL_NS,
                                      "order",
                                      ExpandUtil.TMPL_PREFIX,
                                      index,
                                      pf.getName().XSD_STRING).asInstanceOf[Attribute];
        } else {
          attrs
        }
    }
     
  def doExpand(s: WasAttributedTo, env: Hashtable[QualifiedName, QualifiedName], env2: Hashtable[QualifiedName, java.util.List[TypedValue]], bindings:Bindings):Option[WasAttributedTo] = {
      
    val (updated1, n_id)       = doExpand(s.id,     env, bindings)
    val (updated2, n_entity)   = doExpand(s.entity, env, bindings)
    val (updated3, n_agent)    = doExpand(s.agent,  env, bindings)
    val (updated4, n_attrs,ta) = expandAttributes(s, env2)

    val updated = updated1 || updated2 || updated3 || updated4
    val allUpdated = updated1 && updated2 && updated3 
    allExpanded=allExpanded && allUpdated

    val n_attrs2 = addLabelAttribute(ta,n_attrs)
    val n_attrs3 = if (updated) addOrderAttribute(n_attrs2) else n_attrs2;
    
    if (!allUpdatedRequired || allUpdated) {
        val wat  = pf.newWasAttributedTo(n_id, n_entity, n_agent,n_attrs3);
        Some(wat)
    } else {
        None
    }
    
  }
  
  def doExpand(s: WasAssociatedWith, env: Hashtable[QualifiedName, QualifiedName], env2: Hashtable[QualifiedName, java.util.List[TypedValue]], bindings:Bindings):Option[WasAssociatedWith] = {
      
    val (updated1, n_id)       = doExpand(s.id,     env, bindings)
    val (updated2, n_activity) = doExpand(s.activity, env, bindings)
    val (updated3, n_agent)    = doExpand(s.agent,  env, bindings)
    val (updated4, n_plan)     = if (s.plan==null) (true,null) else  doExpand(s.plan,  env, bindings)
    val (updated5, n_attrs,ta) = expandAttributes(s, env2)

    val updated = updated1 || updated2 || updated3 || updated4 || updated5
    val allUpdated = updated1 && updated2 && updated3 && updated4
    allExpanded=allExpanded && allUpdated

    val n_attrs2 = addLabelAttribute(ta,n_attrs)
    val n_attrs3 = if (updated) addOrderAttribute(n_attrs2) else n_attrs2;
    
    if (!allUpdatedRequired || allUpdated) {
        val waw  = pf.newWasAssociatedWith(n_id, n_activity, n_agent, n_plan, n_attrs3);
        Some(waw)
    } else {
        None
    }
    
  }
  
  def doExpand(s: ActedOnBehalfOf, env: Hashtable[QualifiedName, QualifiedName], env2: Hashtable[QualifiedName, java.util.List[TypedValue]], bindings:Bindings):Option[ActedOnBehalfOf] = {
      
    val (updated1, n_id)       = doExpand(s.id,     env, bindings)
    val (updated2, n_delegate)    = doExpand(s.delegate,    env, bindings)
    val (updated3, n_responsible) = doExpand(s.responsible, env, bindings)
    val (updated4, n_activity)    = if (s.activity==null) (true,null) else doExpand(s.activity,    env, bindings)
    val (updated5, n_attrs,ta)    = expandAttributes(s, env2)

    val updated = updated1 || updated2 || updated3 || updated4 || updated5
    val allUpdated = updated1 && updated2 && updated3 && updated4
    allExpanded=allExpanded && allUpdated

    val n_attrs2 = addLabelAttribute(ta,n_attrs)
    val n_attrs3 = if (updated) addOrderAttribute(n_attrs2) else n_attrs2;
    
    if (!allUpdatedRequired || allUpdated) {
        val aobo  = pf.newActedOnBehalfOf(n_id, n_delegate, n_responsible, n_activity, n_attrs3);
        Some(aobo)
    } else {
        None
    }
    
  }
  
  def doExpand(s: WasDerivedFrom, env: Hashtable[QualifiedName, QualifiedName], env2: Hashtable[QualifiedName, java.util.List[TypedValue]], bindings:Bindings):Option[WasDerivedFrom] = {
      
    val (updated1, n_id)        = doExpand(s.id,     env, bindings)
    val (updated2, n_generated) = doExpand(s.generatedEntity,  env, bindings)
    val (updated3, n_used)      = doExpand(s.usedEntity,       env, bindings)
    val (updated4, n_activity)   = if (s.activity==null)   (true,null) else doExpand(s.activity,   env, bindings)
    val (updated5, n_generation) = if (s.generation==null) (true,null) else doExpand(s.generation, env, bindings)
    val (updated6, n_usage)      = if (s.usage==null)      (true,null) else doExpand(s.usage,      env, bindings)
    val (updated7, n_attrs,ta)  = expandAttributes(s, env2)

    val updated = updated1 || updated2 || updated3 || updated4 || updated5 || updated6 || updated7
    val allUpdated = updated1 && updated2 && updated3 && updated4 && updated5 && updated6
    allExpanded=allExpanded && allUpdated

    val n_attrs2 = addLabelAttribute(ta,n_attrs)
    val n_attrs3 = if (updated) addOrderAttribute(n_attrs2) else n_attrs2;
    
    if (!allUpdatedRequired || allUpdated) {
        val wdf  = pf.newWasDerivedFrom(n_id, n_generated, n_used, n_activity, n_generation, n_usage, n_attrs3);
        Some(wdf)
    } else {
        None
    }
    
  }
  
  def doExpand(s: WasGeneratedBy, env: Hashtable[QualifiedName, QualifiedName], env2: Hashtable[QualifiedName, java.util.List[TypedValue]], bindings:Bindings):Option[WasGeneratedBy] = {
      
    val (updated1, n_id)       = doExpand(s.id,       env, bindings)
    val (updated2, n_entity)   = doExpand(s.entity,   env, bindings)
    val (updated3, n_activity) = doExpand(s.activity, env, bindings)
    val (updated5, n_attrs,ta) = expandAttributes(s, env2)

    val (updated4, n_time)     = (true,ta.time match { case Some(t) => Some(t) case None => s.time}) //TODO, use variable declared as attribute?

    val updated = updated1 || updated2 || updated3 || updated4 || updated5
    val allUpdated = updated1 && updated2 && updated3 && updated4
    allExpanded=allExpanded && allUpdated

    val n_attrs2 = addLabelAttribute(ta,n_attrs)
    val n_attrs3 = if (updated) addOrderAttribute(n_attrs2) else n_attrs2;
    
    if (!allUpdatedRequired || allUpdated) {
        val wgb  = pf.newWasGeneratedBy(n_id, n_entity, n_activity, n_time, n_attrs3);
        Some(wgb)
    } else {
        None
    }  
  }
  
   def doExpand(s: Used, env: Hashtable[QualifiedName, QualifiedName], env2: Hashtable[QualifiedName, java.util.List[TypedValue]], bindings:Bindings):Option[Used] = {
      
    val (updated1, n_id)       = doExpand(s.id,       env, bindings)
    val (updated2, n_activity) = doExpand(s.activity, env, bindings)
    val (updated3, n_entity)   = doExpand(s.entity,   env, bindings)
    val (updated5, n_attrs,ta) = expandAttributes(s, env2)

    val (updated4, n_time)     = (true,ta.time match { case Some(t) => Some(t) case None => s.time})

    val updated = updated1 || updated2 || updated3 || updated4 || updated5
    val allUpdated = updated1 && updated2 && updated3 && updated4
    allExpanded=allExpanded && allUpdated

    val n_attrs2 = addLabelAttribute(ta,n_attrs)
    val n_attrs3 = if (updated) addOrderAttribute(n_attrs2) else n_attrs2;
    
    if (!allUpdatedRequired || allUpdated) {
        val usd  = pf.newUsed(n_id, n_activity, n_entity, n_time, n_attrs3);
        Some(usd)
    } else {
        None
    }  
  }
     
  def doExpand(s: WasInvalidatedBy, env: Hashtable[QualifiedName, QualifiedName], env2: Hashtable[QualifiedName, java.util.List[TypedValue]], bindings:Bindings):Option[WasInvalidatedBy] = {
      
    val (updated1, n_id)       = doExpand(s.id,       env, bindings)
    val (updated2, n_entity)   = doExpand(s.entity,   env, bindings)
    val (updated3, n_activity) = doExpand(s.activity, env, bindings)
    val (updated5, n_attrs,ta) = expandAttributes(s, env2)

    val (updated4, n_time)     = (true,ta.time match { case Some(t) => Some(t) case None => s.time}) //TODO, use variable declared as attribute?

    val updated = updated1 || updated2 || updated3 || updated4 || updated5
    val allUpdated = updated1 && updated2 && updated3 && updated4
    allExpanded=allExpanded && allUpdated

    val n_attrs2 = addLabelAttribute(ta,n_attrs)
    val n_attrs3 = if (updated) addOrderAttribute(n_attrs2) else n_attrs2;
    
    if (!allUpdatedRequired || allUpdated) {
        val wgb  = pf.newWasInvalidatedBy(n_id, n_entity, n_activity, n_time, n_attrs3);
        Some(wgb)
    } else {
        None
    }  
  }
  
  def doExpand(s: WasInformedBy, env: Hashtable[QualifiedName, QualifiedName], env2: Hashtable[QualifiedName, java.util.List[TypedValue]], bindings:Bindings):Option[WasInformedBy] = {
      
    val (updated1, n_id)        = doExpand(s.id,        env, bindings)
    val (updated2, n_informed)  = doExpand(s.informed,  env, bindings)
    val (updated3, n_informant) = doExpand(s.informant, env, bindings)
    val (updated4, n_attrs,ta) = expandAttributes(s, env2)

    val updated = updated1 || updated2 || updated3 || updated4
    val allUpdated = updated1 && updated2 && updated3 
    allExpanded=allExpanded && allUpdated

    val n_attrs2 = addLabelAttribute(ta,n_attrs)
    val n_attrs3 = if (updated) addOrderAttribute(n_attrs2) else n_attrs2
    
    if (!allUpdatedRequired || allUpdated) {
        val wib  = pf.newWasInformedBy(n_id, n_informed, n_informant,n_attrs3);
        Some(wib)
    } else {
        None
    }
    
  }
    def doExpand(s: WasInfluencedBy, env: Hashtable[QualifiedName, QualifiedName], env2: Hashtable[QualifiedName, java.util.List[TypedValue]], bindings:Bindings):Option[WasInfluencedBy] = {
      
    val (updated1, n_id)         = doExpand(s.id,         env, bindings)
    val (updated2, n_influencee) = doExpand(s.influencee, env, bindings)
    val (updated3, n_influencer) = doExpand(s.influencer, env, bindings)
    val (updated4, n_attrs,ta)   = expandAttributes(s, env2)

    val updated = updated1 || updated2 || updated3 || updated4
    val allUpdated = updated1 && updated2 && updated3 
    allExpanded=allExpanded && allUpdated

    val n_attrs2 = addLabelAttribute(ta,n_attrs)
    val n_attrs3 = if (updated) addOrderAttribute(n_attrs2) else n_attrs2;
    
    if (!allUpdatedRequired || allUpdated) {
        val wib  = pf.newWasInfluencedBy(n_id, n_influencee, n_influencer,n_attrs3);
        Some(wib)
    } else {
        None
    }
    
  }
  def doExpand(s: SpecializationOf, env: Hashtable[QualifiedName, QualifiedName], env2: Hashtable[QualifiedName, java.util.List[TypedValue]], bindings:Bindings):Option[SpecializationOf] = {
      
    val (updated1, n_id)       = doExpand(s.id,             env, bindings)
    val (updated2, n_specific) = doExpand(s.specificEntity, env, bindings)
    val (updated3, n_general)  = doExpand(s.generalEntity,  env, bindings)
    val (updated4, n_attrs,ta) = expandAttributes(s, env2)

    val updated = updated1 || updated2 || updated3 || updated4
    val allUpdated = updated1 && updated2 && updated3 
    allExpanded=allExpanded && allUpdated

    val n_attrs2 = addLabelAttribute(ta,n_attrs)
    val n_attrs3 = if (updated) addOrderAttribute(n_attrs2) else n_attrs2
    
    if (!allUpdatedRequired || allUpdated) {
        val spec  = pf.newSpecializationOf(n_id, n_specific, n_general,n_attrs3);
        Some(spec)
    } else {
        None
    }
    
  }
  
  def doExpand(s: AlternateOf, env: Hashtable[QualifiedName, QualifiedName], env2: Hashtable[QualifiedName, java.util.List[TypedValue]], bindings:Bindings):Option[AlternateOf] = {
      
    val (updated1, n_id)       = doExpand(s.id,         env, bindings)
    val (updated2, n_alt1)     = doExpand(s.alternate1, env, bindings)
    val (updated3, n_alt2)     = doExpand(s.alternate2, env, bindings)
    val (updated4, n_attrs,ta) = expandAttributes(s, env2)

    val updated = updated1 || updated2 || updated3 || updated4
    val allUpdated = updated1 && updated2 && updated3 
    allExpanded=allExpanded && allUpdated

    val n_attrs2 = addLabelAttribute(ta,n_attrs)
    val n_attrs3 = if (updated) addOrderAttribute(n_attrs2) else n_attrs2;
    
    if (!allUpdatedRequired || allUpdated) {
        val alt  = pf.newAlternateOf(n_id, n_alt1, n_alt2,n_attrs3);
        Some(alt)
    } else {
        None
    }
    
  }
  
  def doExpand(s: HadMember, env: Hashtable[QualifiedName, QualifiedName], env2: Hashtable[QualifiedName, java.util.List[TypedValue]], bindings:Bindings):Option[HadMember] = {
      
    val (updated1, n_id)       = doExpand(s.id,         env, bindings)
    val (updated2, n_coll)     = doExpand(s.collection, env, bindings)
    val (updated3, n_ent)     = doExpand(s.entity.head, env, bindings)
    val (updated4, n_attrs,ta) = expandAttributes(s, env2)

    val updated = updated1 || updated2 || updated3 || updated4
    val allUpdated = updated1 && updated2 && updated3 
    allExpanded=allExpanded && allUpdated

    val n_attrs2 = addLabelAttribute(ta,n_attrs)
    val n_attrs3 = if (updated) addOrderAttribute(n_attrs2) else n_attrs2;
    
    if (!allUpdatedRequired || allUpdated) {
        val mem  = pf.newHadMember(n_id, n_coll, Set(n_ent),n_attrs3);
        Some(mem)
    } else {
        None
    }
    
  }
  
    
  def doExpand(s: WasStartedBy, env: Hashtable[QualifiedName, QualifiedName], env2: Hashtable[QualifiedName, java.util.List[TypedValue]], bindings:Bindings):Option[WasStartedBy] = {
      
    val (updated1, n_id)       = doExpand(s.id,       env, bindings)
    val (updated2, n_activity) = doExpand(s.activity,   env, bindings)
    val (updated3, n_trigger)  = doExpand(s.trigger,   env, bindings)
    val (updated4, n_starter)  = doExpand(s.starter, env, bindings)
    val (updated6, n_attrs,ta) = expandAttributes(s, env2)

    val (updated5, n_time)     = (true,ta.time match { case Some(t) => Some(t) case None => s.time}) //TODO, use variable declared as attribute?

    val updated = updated1 || updated2 || updated3 || updated4 || updated5 || updated6
    val allUpdated = updated1 && updated2 && updated3 && updated4 && updated5
    allExpanded=allExpanded && allUpdated

    val n_attrs2 = addLabelAttribute(ta,n_attrs)
    val n_attrs3 = if (updated) addOrderAttribute(n_attrs2) else n_attrs2;
    
    if (!allUpdatedRequired || allUpdated) {
        val wsb  = pf.newWasStartedBy(n_id, n_activity, n_trigger, n_starter, n_time, n_attrs3);
        Some(wsb)
    } else {
        None
    }  
  }
  def addLabelAttribute(ta: TemplateAttributeValues,set: Set[Attribute]): Set[Attribute] = {
    ta.label match {
      case Some(lab) => set + lab
      case None => set
    }
  }
  
  def doExpand(s: Entity, env: Hashtable[QualifiedName, QualifiedName], env2: Hashtable[QualifiedName, java.util.List[TypedValue]], bindings:Bindings):Option[Entity] = {
    
 
    val (updated1, n_id)        = doExpand(s.id,      env, bindings)
    val (updated2, n_attrs, ta) = expandAttributes(s, env2)

    val updated = updated1 || updated2 
    val allUpdated = updated1 ;
    allExpanded=allExpanded && allUpdated;
    val n_attrs2 = addLabelAttribute(ta,n_attrs)
    val n_attrs3 = if (updated) addOrderAttribute(n_attrs2) else n_attrs2;
    
    if (!allUpdatedRequired || allUpdated) {
        val ent  = pf.newEntity(n_id,n_attrs3);
        Some(ent)
    } else {
        None
    }
  }
  
  def doExpand(s: Agent, env: Hashtable[QualifiedName, QualifiedName], env2: Hashtable[QualifiedName, java.util.List[TypedValue]], bindings:Bindings):Option[Agent] = {
    
 
    val (updated1, n_id)        = doExpand(s.id,      env, bindings)
    val (updated2, n_attrs, ta) = expandAttributes(s, env2)

    val updated = updated1 || updated2 
    val allUpdated = updated1 ;
    allExpanded=allExpanded && allUpdated;
    val n_attrs2 = addLabelAttribute(ta,n_attrs)
    val n_attrs3 = if (updated) addOrderAttribute(n_attrs2) else n_attrs2;
    
    if (!allUpdatedRequired || allUpdated) {
        val ag  = pf.newAgent(n_id,n_attrs3);
        Some(ag)
    } else {
        None
    }
  }
  
  def doExpand(s: Activity, env: Hashtable[QualifiedName, QualifiedName], env2: Hashtable[QualifiedName, java.util.List[TypedValue]], bindings:Bindings):Option[Activity] = {
    
 
    val (updated1, n_id)        = doExpand(s.id,      env, bindings)
    val (updated2, n_attrs, ta) = expandAttributes(s, env2)
    val (updated3, n_start)     = (true,ta.start match { case Some(t) => Some(t) case None => s.startTime}) 
    val (updated4, n_end)       = (true,ta.end   match { case Some(t) => Some(t) case None => s.endTime}) 
    val updated = updated1 || updated2 || updated3 || updated4 
    val allUpdated = updated1 && updated3 && updated4  
    allExpanded=allExpanded && allUpdated;
    val n_attrs2 = addLabelAttribute(ta,n_attrs)
    val n_attrs3 = if (updated) addOrderAttribute(n_attrs2) else n_attrs2;
    
    if (!allUpdatedRequired || allUpdated) {
        val act  = pf.newActivity(n_id,n_start,n_end,n_attrs3);
        Some(act)
    } else {
        None
    }
  }
  
  
}



class ExpandActionLegacy(pf: org.openprovenance.prov.model.ProvFactory,
                         u: org.openprovenance.prov.model.ProvUtilities,
                         expand: Expander,
                         envLegacy: Hashtable[org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName],
                         envLegacy2: Hashtable[org.openprovenance.prov.model.QualifiedName, java.util.List[org.openprovenance.prov.model.TypedValue]],
                         index: java.util.List[Integer],
                         bindings: Bindings,
                         grp1: Groupings,
                         addOrderp: Boolean,
                         allUpdatedRequired:Boolean) extends StatementAction { //extends ExpandAction(pf,u,expand,envLegacy,envLegacy2,index,bindings,grp1,addOrderp,allUpdatedRequired) {
  
    
    val ll: java.util.List[org.openprovenance.prov.model.StatementOrBundle] = new java.util.LinkedList[org.openprovenance.prov.model.StatementOrBundle]();
    def getList() = ll
    
    var allExpanded=true;
    def  getAllExpanded() = {
        allExpanded;
    }
    
    val env=envLegacy.asInstanceOf[Hashtable[QualifiedName, QualifiedName]]
    val env2=envLegacy2.asInstanceOf[Hashtable[QualifiedName, java.util.List[TypedValue]]]
 

    override def doAction(s: org.openprovenance.prov.model.Activity):Unit = { 
          val te=new TermExpander(allUpdatedRequired,addOrderp,index)
          val res=te.doExpand(s.asInstanceOf[Activity], env, env2, bindings)

          allExpanded=allExpanded && te.allExpanded
          
          res match {
            case Some(t) => ll.add(t)
            case None =>        
          }
    }     
      
    
    override def doAction(s: org.openprovenance.prov.model.Used):Unit = { 
          val te=new TermExpander(allUpdatedRequired,addOrderp,index)
          val res=te.doExpand(s.asInstanceOf[Used], env, env2, bindings)

          allExpanded=allExpanded && te.allExpanded
          
          res match {
            case Some(t) => ll.add(t)
            case None =>        
          }
    }     
      
    
    override def doAction(s: org.openprovenance.prov.model.WasStartedBy):Unit = { 
    	    val te=new TermExpander(allUpdatedRequired,addOrderp,index)
          val res=te.doExpand(s.asInstanceOf[WasStartedBy], env, env2, bindings)

          allExpanded=allExpanded && te.allExpanded
          
          res match {
            case Some(t) => ll.add(t)
            case None =>        
          }
    }
    
    override def doAction(s: org.openprovenance.prov.model.Agent):Unit = { 
          val te=new TermExpander(allUpdatedRequired,addOrderp,index)
          val res=te.doExpand(s.asInstanceOf[Agent], env, env2, bindings)

          allExpanded=allExpanded && te.allExpanded
          
          res match {
            case Some(t) => ll.add(t)
            case None =>        
          }
    }     
         
    
    override def doAction(s: org.openprovenance.prov.model.AlternateOf):Unit = {
          val te=new TermExpander(allUpdatedRequired,addOrderp,index)
          val res=te.doExpand(s.asInstanceOf[AlternateOf], env, env2, bindings)

          allExpanded=allExpanded && te.allExpanded
          
          res match {
            case Some(t) => ll.add(t)
            case None =>        
          }
    }    
      
    
    override def doAction(s: org.openprovenance.prov.model.WasAssociatedWith):Unit = { 
          val te=new TermExpander(allUpdatedRequired,addOrderp,index)
          val res=te.doExpand(s.asInstanceOf[WasAssociatedWith], env, env2, bindings)

          allExpanded=allExpanded && te.allExpanded
          
          res match {
            case Some(t) => ll.add(t)
            case None =>        
          }
    }
    override def doAction(s: org.openprovenance.prov.model.WasAttributedTo):Unit = { 
          val te=new TermExpander(allUpdatedRequired,addOrderp,index)
          val res=te.doExpand(s.asInstanceOf[WasAttributedTo], env, env2, bindings)

          allExpanded=allExpanded && te.allExpanded
          
          res match {
            case Some(t) => ll.add(t)
            case None =>        
          }
    }
    override def doAction(s: org.openprovenance.prov.model.WasInfluencedBy):Unit = { 
          val te=new TermExpander(allUpdatedRequired,addOrderp,index)
          val res=te.doExpand(s.asInstanceOf[WasInfluencedBy], env, env2, bindings)

          allExpanded=allExpanded && te.allExpanded
          
          res match {
            case Some(t) => ll.add(t)
            case None =>        
          }
    }  
    
    override def doAction(s: org.openprovenance.prov.model.ActedOnBehalfOf):Unit = { 
          val te=new TermExpander(allUpdatedRequired,addOrderp,index)
          val res=te.doExpand(s.asInstanceOf[ActedOnBehalfOf], env, env2, bindings)

          allExpanded=allExpanded && te.allExpanded
          
          res match {
            case Some(t) => ll.add(t)
            case None =>        
          }
    }     
      
    
    override def doAction(s: org.openprovenance.prov.model.WasDerivedFrom):Unit = { 
          val te=new TermExpander(allUpdatedRequired,addOrderp,index)
          val res=te.doExpand(s.asInstanceOf[WasDerivedFrom], env, env2, bindings)

          allExpanded=allExpanded && te.allExpanded
          
          res match {
            case Some(t) => ll.add(t)
            case None =>        
          }
    }     
     
      
    override def doAction(s: org.openprovenance.prov.model.DictionaryMembership):Unit = { 
            throw new UnsupportedOperationException
    }
    override def doAction(s: org.openprovenance.prov.model.DerivedByRemovalFrom):Unit = { 
            throw new UnsupportedOperationException
    }
    override def doAction(s: org.openprovenance.prov.model.WasEndedBy):Unit = { 
            throw new UnsupportedOperationException

    }
    override def doAction(e: org.openprovenance.prov.model.Entity):Unit = { 
        val te=new TermExpander(allUpdatedRequired,addOrderp,index)
        val res=te.doExpand(e.asInstanceOf[Entity], env, env2, bindings)

        allExpanded=allExpanded && te.allExpanded
          
        res match {
          case Some(t) => ll.add(t)
          case None =>        
        }
    }
    
    override def doAction(s: org.openprovenance.prov.model.WasGeneratedBy):Unit = { 
          val te=new TermExpander(allUpdatedRequired,addOrderp,index)
          val res=te.doExpand(s.asInstanceOf[WasGeneratedBy], env, env2, bindings)

          allExpanded=allExpanded && te.allExpanded
          
          res match {
            case Some(t) => ll.add(t)
            case None =>        
          }
    }
    
    override def doAction(s: org.openprovenance.prov.model.WasInvalidatedBy):Unit = {
    	    val te=new TermExpander(allUpdatedRequired,addOrderp,index)
          val res=te.doExpand(s.asInstanceOf[WasInvalidatedBy], env, env2, bindings)

          allExpanded=allExpanded && te.allExpanded
          
          res match {
            case Some(t) => ll.add(t)
            case None =>        
          }
    }
    
    override def doAction(s: org.openprovenance.prov.model.HadMember):Unit = { 
       	  val te=new TermExpander(allUpdatedRequired,addOrderp,index)
       	  val res=te.doExpand(s.asInstanceOf[HadMember], env, env2, bindings)

          allExpanded=allExpanded && te.allExpanded
          
          res match {
            case Some(t) => ll.add(t)
            case None =>        
          }
    }   

    override def doAction(s: org.openprovenance.prov.model.MentionOf):Unit = { 
            throw new UnsupportedOperationException
    }
    
    override def doAction(s: org.openprovenance.prov.model.SpecializationOf):Unit = { 
          val te=new TermExpander(allUpdatedRequired,addOrderp,index)
          val res=te.doExpand(s.asInstanceOf[SpecializationOf], env, env2, bindings)

          allExpanded=allExpanded && te.allExpanded
          
          res match {
            case Some(t) => ll.add(t)
            case None =>        
          }
    }
    
    override def doAction(s: org.openprovenance.prov.model.extension.QualifiedSpecializationOf):Unit = {
       	  val te=new TermExpander(allUpdatedRequired,addOrderp,index)
          val res=te.doExpand(s.asInstanceOf[SpecializationOf], env, env2, bindings)

          allExpanded=allExpanded && te.allExpanded
          
          res match {
            case Some(t) => ll.add(t)
            case None =>        
          }
    }   
    
    override def doAction(s: org.openprovenance.prov.model.extension.QualifiedAlternateOf):Unit = { 
          val te=new TermExpander(allUpdatedRequired,addOrderp,index)
          val res=te.doExpand(s.asInstanceOf[AlternateOf], env, env2, bindings)

          allExpanded=allExpanded && te.allExpanded
          
          res match {
            case Some(t) => ll.add(t)
            case None =>        
          }
    }   
   
    
    override def doAction(s: org.openprovenance.prov.model.extension.QualifiedHadMember):Unit = { 
          val te=new TermExpander(allUpdatedRequired,addOrderp,index)
       	  val res=te.doExpand(s.asInstanceOf[HadMember], env, env2, bindings)

          allExpanded=allExpanded && te.allExpanded
          
          res match {
            case Some(t) => ll.add(t)
            case None =>        
          }
    }   
       
    override def doAction(s: org.openprovenance.prov.model.DerivedByInsertionFrom):Unit = { 
                  throw new UnsupportedOperationException
    }
    
    override def doAction(s: org.openprovenance.prov.model.WasInformedBy):Unit = { 
          val te=new TermExpander(allUpdatedRequired,addOrderp,index)
          val res=te.doExpand(s.asInstanceOf[WasInformedBy], env, env2, bindings)

          allExpanded=allExpanded && te.allExpanded
          
          res match {
            case Some(t) => ll.add(t)
            case None =>        
          }
    }   
   
    
    
    
    def doAction(bun: org.openprovenance.prov.model.Bundle, provUtilities: ProvUtilities):Unit = { 
        import TermExpander.getUUIDQualifiedName
        
        val statements:java.util.List[org.openprovenance.prov.model.Statement]= bun.getStatement();
        val newStatements:java.util.List[org.openprovenance.prov.model.Statement] = new java.util.LinkedList[org.openprovenance.prov.model.Statement]();

        for (s <- statements) {
            for (sb: org.openprovenance.prov.model.StatementOrBundle <- expand.expand(s, bindings, grp1)) {
                newStatements.add(sb.asInstanceOf[org.openprovenance.prov.model.Statement]);
            }

        }

        updateEnvironmentForBundleId(bun, bindings, env);

        var newId: org.openprovenance.prov.model.QualifiedName=null
        
        val bunId = bun.getId()
        if (ExpandUtil.isVariable(bunId)) {
            // System.out.println("===> bundle " + env + " " + bindings);
            val value = env.get(bunId);
            if (value != null) {
                newId = value;
            } else {
                if (ExpandUtil.isGensymVariable(bunId)) {
                    val uuid = getUUIDQualifiedName();
                    newId = uuid;
                    bindings.addVariable(bunId, uuid);
                } else {
                    newId = bunId;
                }
            }
        } else {
            newId = bunId;
        }
        val tmpBun=pf.newNamedBundle(newId, new Namespace, newStatements)
        val ns=Namespace.gatherNamespaces(tmpBun,pf)
        ll.add(pf.newNamedBundle(newId, ns, newStatements))

    }



    def updateEnvironmentForBundleId(bun: org.openprovenance.prov.model.Bundle,
                                     bindings1: Bindings,
                                     env0: Hashtable[QualifiedName, QualifiedName]) {
        val id = bun.getId();
        if (ExpandUtil.isVariable(id)) {
            val vals= bindings1.getVariables().get(id);
            if (vals == null) {
                if (ExpandUtil.isGensymVariable(id)) {
                    // OK, we'll generate a uuid later
                } else {
                    throw new BundleVariableHasNoValue(id);
                }
            } else {
                if (vals.size() > 1) {
                    throw new BundleVariableHasMultipleValues(id, vals);
                } else {
                    env0.put(id.asInstanceOf[QualifiedName], vals.get(0).asInstanceOf[QualifiedName]);
                }
            }
        }
    }
   


    
}

class Expander(allUpdatedRequired:Boolean,addOrderp:Boolean) {
import TermExpander.pu
  var allExpanded: Boolean=true
  def getAllExpanded() = allExpanded
  
   def  expand(bun: org.openprovenance.prov.model.Bundle, bindings1: Bindings, grp1: Groupings): java.util.List[org.openprovenance.prov.model.StatementOrBundle] = {
        val env0: Hashtable[org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName] = new Hashtable[org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName]();
        val env1: Hashtable[org.openprovenance.prov.model.QualifiedName, java.util.List[org.openprovenance.prov.model.TypedValue]] = new Hashtable[org.openprovenance.prov.model.QualifiedName, java.util.List[org.openprovenance.prov.model.TypedValue]]();

        val action = new ExpandActionLegacy(ProvFactory.pf,
                                            pu,
                                            this,
                                            env0,
                                            env1,
                                            null,
                                            bindings1,
                                            grp1,
                                            addOrderp,
                                            allUpdatedRequired)
        pu.doAction(bun, action);
        allExpanded=allExpanded && action.getAllExpanded();
        return action.getList();
    }
  
  
    def expand(statement: org.openprovenance.prov.model.Statement, bindings1: Bindings, grp1: Groupings) : java.util.List[org.openprovenance.prov.model.StatementOrBundle]= {
        val us1 = ExpandUtil.usedGroups(statement, grp1, bindings1)
        expand(statement, bindings1, grp1, us1)
    }
    
  

    def expand(statement: org.openprovenance.prov.model.Statement,
               bindings1: Bindings ,
               grp1: Groupings,
               us1: Using): java.util.List[org.openprovenance.prov.model.StatementOrBundle] = {
        val results: java.util.List[org.openprovenance.prov.model.StatementOrBundle] = new java.util.LinkedList[org.openprovenance.prov.model.StatementOrBundle]()
        val iter = us1.getUsingIterator()

        while (iter.hasNext()) {
            val index:java.util.List[Integer] = iter.next()
            // System.out.println(" Index " + index);

            val env = us1.get(bindings1, grp1, index)
            val env2 = us1.getAttr(ExpandUtil.freeAttributeVariables(statement, ProvFactory.pf),
                                   bindings1,
                                   iter)

            val action = new ExpandActionLegacy(ProvFactory.pf,
                                                pu,
                                                this,
                                                env,
                                                env2,
                                                index,
                                                bindings1,
                                                grp1,
                                                addOrderp,
                                                allUpdatedRequired);
            pu.doAction(statement, action);
            allExpanded=allExpanded && action.getAllExpanded();

            results.addAll(action.getList());

        }
        return results;

    }
    
    def expander(docIn: Document, out: String, docBindings: Document):Document= {
      expander(docIn,out,Bindings.fromDocument(docBindings, ProvFactory.pf))
    }
    

    def expander(docIn: Document, out: String, docBindings: Document, kind: BindingKind.Value):Document= {
      kind match {
        case BindingKind.V1 => expander(docIn,out,Bindings.fromDocument(docBindings, ProvFactory.pf))
        case BindingKind.V2 => expander(docIn,out,Bindings.fromDocument_v2(docBindings, ProvFactory.pf))        
      }
    }
    
    def expander(docIn: Document, out: String, bindings: Bindings):Document= {

        val bun= docIn.getStatementOrBundle().get(0).asInstanceOf[Bundle];

        val grp1 = Groupings.fromDocument(docIn);

        val bun1 = expand(bun, bindings, grp1).get(0).asInstanceOf[Bundle];
        
        val docNS=new Namespace
        docNS.addKnownNamespaces()
        bun1.namespace.setParent(docNS)
        
        new Document(Seq(bun1),docNS)
       

    }


    

}

object BindingKind extends Enumeration {
	val V1, V2 = Value
}


