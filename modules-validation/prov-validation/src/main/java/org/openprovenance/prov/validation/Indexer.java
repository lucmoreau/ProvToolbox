package org.openprovenance.prov.validation;

import java.util.HashSet;
import java.util.List;
import java.util.Hashtable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;

import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.extension.QualifiedAlternateOf;
import org.openprovenance.prov.model.extension.QualifiedHadMember;
import org.openprovenance.prov.model.extension.QualifiedSpecializationOf;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Indexer {

	static Logger logger = LogManager.getLogger(Indexer.class);
	final  ProvUtilities u = new ProvUtilities();

	public final Gensym g;

	public final Namespace namespace = new Namespace();
	Merger merger;
	final ProvFactory p;



	public Indexer(ProvFactory p, ObjectMaker om) {
		this.p=p;
		this.g = new Gensym(namespace, p,om);
		registerNamespaces(namespace);
		this.merger = new Merger(p, u, qualifiedNameMismatch, this);
        mentionOfTable = new Hashtable<>();
    }

	public void registerNamespaces(Namespace namespace) {
		namespace.register(NamespacePrefixMapper.XSD_PREFIX,
				NamespacePrefixMapper.XSD_NS);
		namespace.register(Gensym.VAL_PREFIX, g.this_VAL_URI);
		namespace.registerDefault(g.this_VAL_URI);
		namespace.register("ex", "http://example.org/");
		// TODO: hard coded namespace for example!
	}

	public void index(Document doc) {
		List<Statement> recs = u.getStatement(doc);
		index(recs);
	}


	public void index(Bundle bundle) {
		List<Statement> recs = u.getStatement(bundle);
		index(recs);
	}

	class IndexAction implements StatementAction {


		public void doAction(Entity e) {

			String uri = e.getId().getUri();
			Entity entry = entityTable.get(uri);
			if (entry == null) {
				entry = p.newEntity(e); // create a fresh copy TODO: why?
				p.addAttributes(e, entry);
				entityTable.put(uri, entry);
			} else {
				entityTable.put(uri, resolveDuplicate(e, entry));
			}
			addTypeAttributes(e);
		}


		public void doAction(Activity a) {
			String uri = a.getId().getUri();
			Activity entry = activityTable.get(uri);
			if (entry == null) {
				activityTable.put(uri, a);
			} else {
				activityTable.put(uri, resolveDuplicate(a, entry));
			}
			addTypeAttributes(a);
		}


		public void doAction(Agent ag) {
			String uri = ag.getId().getUri();
			Agent entry = agentTable.get(uri);
			if (entry == null) {
				agentTable.put(uri, ag);
			} else {
				agentTable.put(uri, resolveDuplicate(ag, entry));
			}
			addTypeAttributes(ag);
		}


		public void doAction(WasGeneratedBy gen) {
			if (gen.getId() == null) g.setId(gen);
			String uri = gen.getId().getUri();
			WasGeneratedBy entry = wasGeneratedByTable.get(uri);
			if (entry == null) {
				wasGeneratedByTable.put(uri, gen);
			} else {
				wasGeneratedByTable.put(uri, resolveDuplicate(gen, entry));
			}
			addTypeAttributes(gen);

		}


		public void doAction(Used use) {
			if (use.getId() == null) g.setId(use);
			String uri = use.getId().getUri();
			Used entry = usedTable.get(uri);
			if (entry == null) {
				usedTable.put(uri, use);
			} else {
				usedTable.put(uri, resolveDuplicate(use, entry));
			}
			addTypeAttributes(use);

		}


		public void doAction(WasInvalidatedBy inv) {
			if (inv.getId() == null) g.setId(inv);
			String uri = inv.getId().getUri();
			WasInvalidatedBy entry = wasInvalidatedByTable.get(uri);
			if (entry == null) {
				wasInvalidatedByTable.put(uri, inv);
			} else {
				wasInvalidatedByTable.put(uri, resolveDuplicate(inv, entry));
			}
			addTypeAttributes(inv);
		}


		public void doAction(WasInformedBy inf) {
			if (inf.getId() == null) g.setId(inf);
			String uri = inf.getId().getUri();
			WasInformedBy entry = wasInformedByTable.get(uri);
			if (entry == null) {
				wasInformedByTable.put(uri, inf);
			} else {
				wasInformedByTable.put(uri, resolveDuplicate(inf, entry));
			}
			addTypeAttributes(inf);
		}


		public void doAction(WasStartedBy start) {
			if (start.getId() == null) g.setId(start);
			String uri = start.getId().getUri();
			WasStartedBy entry = wasStartedByTable.get(uri);
			if (entry == null) {
				wasStartedByTable.put(uri, start);
			} else {
				logger.debug("index(): calling resolveDuplicate  ++++++++ ");
				wasStartedByTable.put(uri, resolveDuplicate(start, entry));
			}
			addTypeAttributes(start);

		}


		public void doAction(WasEndedBy end) {
			if (end.getId() == null) g.setId(end);
			String uri = end.getId().getUri();
			WasEndedBy entry = wasEndedByTable.get(uri);
			if (entry == null) {
				wasEndedByTable.put(uri, end);
			} else {
				wasEndedByTable.put(uri, resolveDuplicate(end, entry));
			}
			addTypeAttributes(end);

		}


		public void doAction(WasDerivedFrom der) {
			if (der.getId() == null) g.setId(der);
			String uri = der.getId().getUri();
			WasDerivedFrom entry = wasDerivedFromTable.get(uri);
			if (entry == null) {
				wasDerivedFromTable.put(uri, der);
			} else {
				wasDerivedFromTable.put(uri, resolveDuplicate(der, entry));
			}
			addTypeAttributes(der);

		}

		public void doAction(WasAssociatedWith assoc) {
			if (assoc.getId() == null) g.setId(assoc);
			String uri = assoc.getId().getUri();
			WasAssociatedWith entry = wasAssociatedWithTable.get(uri);
			if (entry == null) {
				wasAssociatedWithTable.put(uri, assoc);
			} else {
				wasAssociatedWithTable.put(uri, resolveDuplicate(assoc, entry));
			}
			addTypeAttributes(assoc);
		}



		public void doAction(WasAttributedTo attr) {
			if (attr.getId() == null) g.setId(attr);
			String uri = attr.getId().getUri();
			WasAttributedTo entry = wasAttributedToTable.get(uri);
			if (entry == null) {
				wasAttributedToTable.put(uri, attr);
			} else {
				wasAttributedToTable.put(uri, resolveDuplicate(attr, entry));
			}
			addTypeAttributes(attr);

		}


		public void doAction(ActedOnBehalfOf del) {
			if (del.getId() == null) g.setId(del);
			String uri = del.getId().getUri();
			ActedOnBehalfOf entry = actedOnBehalfOfTable.get(uri);
			if (entry == null) {
				actedOnBehalfOfTable.put(uri, del);
			} else {
				actedOnBehalfOfTable.put(uri, resolveDuplicate(del, entry));
			}
			addTypeAttributes(del);

		}


		public void doAction(WasInfluencedBy infl) {
			if (infl.getId() == null) g.setId(infl);
			String uri = infl.getId().getUri();
			WasInfluencedBy entry = wasInfluencedByTable.get(uri);
			if (entry == null) {
				wasInfluencedByTable.put(uri, infl);
			} else {
				wasInfluencedByTable.put(uri, resolveDuplicate(infl, entry));
			}
			addTypeAttributes(infl);
		}


		public void doAction(AlternateOf alt) {
			alternateOfList.add(alt);
		}


		public void doAction(SpecializationOf spec) {
			specializationOfList.add(spec);

		}

		public void doAction(QualifiedAlternateOf alt) {
			alternateOfList.add(alt);
		}


		public void doAction(QualifiedSpecializationOf spec) {
			specializationOfList.add(spec);

		}

		public void doAction(MentionOf men) {
			String uri = men.getSpecificEntity().getUri();
			MentionOf entry = mentionOfTable.get(uri);
			if (entry == null) {
				mentionOfTable.put(uri, men);
			} else {
				mentionOfTable.put(uri, resolveDuplicate(men, entry));
			}
		}


		public void doAction(HadMember mem) {
			logger.debug("!!!!!!!!!!!!!!!!!!!!!adding " + mem);
			membershipList.add(mem);

		}

		public void doAction(QualifiedHadMember mem) {
			logger.debug("!!!!!!!!!!!!!!!!!!!!!adding " + mem);
			membershipList.add(mem);
		}


		public void doAction(DictionaryMembership s) {
		}


		public void doAction(DerivedByRemovalFrom s) {
		}


		public void doAction(DerivedByInsertionFrom s) {
		}


		public void doAction(Bundle s,
							 org.openprovenance.prov.model.ProvUtilities provUtilities) {
		}

	}

	public <T extends Statement> void addTypeAttributes(T entry) {
		addTypeAttributes(getTypeTable(entry), entry);
	}

	public <T extends Statement> Hashtable<String,Set<Type>> getTypeTable(T entry) {
		Object o=u.doAction(entry, new TypeTable());
		@SuppressWarnings("unchecked")
		Hashtable<String,Set<Type>> table=(Hashtable<String,Set<Type>>) o;
		return table;
	}

	public class TypeTable implements StatementActionValue {

		public Object doAction(Entity e) {
			return entityTypeTable;
		}


		public Object doAction(Activity a) {
			return activityTypeTable;
		}


		public Object doAction(Agent ag) {
			return agentTypeTable;
		}


		public Object doAction(WasGeneratedBy gen) {
			return wasGeneratedByTypeTable;
		}


		public Object doAction(Used use) {
			return usedTypeTable;
		}


		public Object doAction(WasInvalidatedBy inv) {
			return wasInvalidatedByTypeTable;
		}


		public Object doAction(WasStartedBy start) {
			return wasStartedByTypeTable;
		}

		public Object doAction(WasEndedBy end) {
			return wasEndedByTypeTable;
		}


		public Object doAction(WasInformedBy inf) {
			return wasInformedByTypeTable;
		}


		public Object doAction(WasDerivedFrom der) {
			return wasDerivedFromTypeTable;
		}


		public Object doAction(WasAssociatedWith assoc) {
			return wasAssociatedWithTypeTable;
		}


		public Object doAction(WasAttributedTo attr) {
			return wasAttributedToTypeTable;
		}


		public Object doAction(ActedOnBehalfOf del) {
			return actedOnBehalfOfTypeTable;
		}


		public Object doAction(WasInfluencedBy inf) {
			return wasInfluencedByTypeTable;
		}


		public Object doAction(AlternateOf alt) {
			// no attributes
			throw new UnsupportedOperationException();
		}


		public Object doAction(MentionOf men) {
			// no attributes
			throw new UnsupportedOperationException();
		}


		public Object doAction(SpecializationOf spec) {
			// no attributes
			throw new UnsupportedOperationException();
		}

		public Object doAction(QualifiedSpecializationOf spec) {
			// no attributes
			throw new UnsupportedOperationException();
		}

		public Object doAction(QualifiedHadMember mem) {
			// no attributes
			throw new UnsupportedOperationException();
		}

		public Object doAction(QualifiedAlternateOf mem) {
			// no attributes
			throw new UnsupportedOperationException();
		}

		public Object doAction(HadMember mem) {
			throw new UnsupportedOperationException();

		}


		public Object doAction(DictionaryMembership s) {
			return null;
		}


		public Object doAction(DerivedByRemovalFrom s) {
			return null;
		}


		public Object doAction(DerivedByInsertionFrom s) {
			return null;
		}


		public Object doAction(Bundle s,
							   org.openprovenance.prov.model.ProvUtilities provUtilities) {
			return null;
		}
	}


	public void index(List<Statement> recs) {
		u.forAllStatement(recs, new IndexAction());
	}

	public <T extends Statement> void addTypeAttributes(Hashtable<String, Set<Type>> typeTable,
														T e) {
		if (e instanceof Identifiable) {
			if (e instanceof HasType) {
				HasType o=(HasType)e;
				String id=((Identifiable) o).getId().getUri();
				Set<Type> entry=typeTable.get(id);
				if (entry==null) {
					entry= new HashSet<>();
					typeTable.put(id, entry);
				}
				entry.addAll(o.getType());
			}
		}
	}

	public String summary() {
		List<String> el = new LinkedList<>(entityTable.keySet());
		Collections.sort(el);
		List<String> agl = new LinkedList<>(agentTable.keySet());
		Collections.sort(agl);
		List<String> al = new LinkedList<>(activityTable.keySet());
		Collections.sort(al);
		List<String> ul = new LinkedList<>(usedTable.keySet());
		Collections.sort(ul);
		List<String> gl = new LinkedList<>(wasGeneratedByTable.keySet());
		Collections.sort(gl);
		List<String> il = new LinkedList<>(wasInvalidatedByTable.keySet());
		Collections.sort(il);
		List<String> sl = new LinkedList<>(wasStartedByTable.keySet());
		Collections.sort(sl);
		List<String> edl = new LinkedList<>(wasEndedByTable.keySet());
		Collections.sort(edl);
		List<String> dl = new LinkedList<>(wasDerivedFromTable.keySet());
		Collections.sort(dl);
		List<String> wibl = new LinkedList<>(wasInformedByTable.keySet());
		Collections.sort(wibl);
		List<String> assocl = new LinkedList<>(wasAssociatedWithTable.keySet());
		Collections.sort(assocl);
		List<String> watl = new LinkedList<>(wasAttributedToTable.keySet());
		Collections.sort(watl);
		List<String> winflbl = new LinkedList<>(wasInfluencedByTable.keySet());
		Collections.sort(watl);
		List<String> aobl = new LinkedList<>(actedOnBehalfOfTable.keySet());
		Collections.sort(aobl);
		return "index: \n" + " entity: " + el + "\n" + " activity: " + al
				+ "\n" + " agent: " + agl + "\n" + " used: " + ul + "\n"
				+ " wasGeneratedBy: " + gl + "\n" + " wasInvalidatedBy: " + il
				+ "\n" + " wasStartedBy: " + sl + "\n" + " wasEndedBy: " + edl
				+ "\n" + " wasInformedBy: " + wibl + "\n"
				+ " wasAssociatedWith: " + assocl + "\n" + " wasAttributedTo: "
				+ watl + "\n" + " actedOnBehalfOf: " + aobl + "\n"
				+ " wasInfluencedBy: " + winflbl + "\n" + " wasDerivedFrom: "
				+ dl;
	}

	public boolean existentialVariable(String uri) {
		return uri.startsWith(g.this_VAL_URI);
	}

	public boolean existentialVariable(QualifiedName name) {
		return (name instanceof VarQName);
		// return g.this_VAL_URI.equals(qname.getNamespaceURI());
	}



	public Hashtable<String, Entity> entityTable = new Hashtable<>();
	public Hashtable<String, Activity> activityTable = new Hashtable<>();
	public Hashtable<String, Agent> agentTable = new Hashtable<>();
	public Hashtable<String, Used> usedTable = new Hashtable<>();
	public Hashtable<String, WasGeneratedBy> wasGeneratedByTable = new Hashtable<>();
	public Hashtable<String, WasInvalidatedBy> wasInvalidatedByTable = new Hashtable<>();
	public Hashtable<String, WasStartedBy> wasStartedByTable = new Hashtable<>();
	public Hashtable<String, WasEndedBy> wasEndedByTable = new Hashtable<>();
	public Hashtable<String, WasDerivedFrom> wasDerivedFromTable = new Hashtable<>();
	public Hashtable<String, WasInformedBy> wasInformedByTable = new Hashtable<>();
	public Hashtable<String, WasAssociatedWith> wasAssociatedWithTable = new Hashtable<String, WasAssociatedWith>();
	public Hashtable<String, WasAttributedTo> wasAttributedToTable = new Hashtable<>();
	public Hashtable<String, WasInfluencedBy> wasInfluencedByTable = new Hashtable<>();
	public Hashtable<String, ActedOnBehalfOf> actedOnBehalfOfTable = new Hashtable<>();
	public Hashtable<String,MentionOf> mentionOfTable = new Hashtable<>();

	public List<AlternateOf> alternateOfList = new LinkedList<>();
	public List<SpecializationOf> specializationOfList = new LinkedList<>();
	public List<HadMember> membershipList= new LinkedList<>();


	public Hashtable<String, Set<Object>> entityTypeTable = new Hashtable<>();
	public Hashtable<String, Set<Object>> activityTypeTable = new Hashtable<>();
	public Hashtable<String, Set<Object>> agentTypeTable = new Hashtable<>();
	public Hashtable<String, Set<Object>> usedTypeTable = new Hashtable<>();
	public Hashtable<String, Set<Object>> wasGeneratedByTypeTable = new Hashtable<>();
	public Hashtable<String, Set<Object>> wasInvalidatedByTypeTable = new Hashtable<>();
	public Hashtable<String, Set<Object>> wasStartedByTypeTable = new Hashtable<>();
	public Hashtable<String, Set<Object>> wasEndedByTypeTable = new Hashtable<>();
	public Hashtable<String, Set<Object>> wasDerivedFromTypeTable = new Hashtable<>();
	public Hashtable<String, Set<Object>> wasInformedByTypeTable = new Hashtable<>();
	public Hashtable<String, Set<Object>> wasAssociatedWithTypeTable = new Hashtable<>();
	public Hashtable<String, Set<Object>> wasAttributedToTypeTable = new Hashtable<>();
	public Hashtable<String, Set<Object>> wasInfluencedByTypeTable = new Hashtable<>();
	public Hashtable<String, Set<Object>> actedOnBehalfOfTypeTable = new Hashtable<>();



	public <T> void putInTable(Hashtable<Class, Hashtable<String, T>> t,
							   Class<?> c, Hashtable<String, T> e) {
		t.put(c, e);
	}

	/**
	 * TODO: can it be: Hashtable&lt;Class,Hashtable&lt;String,T&gt;&gt;
	 * TODO use this index to make other functions polymorphic.
	 * @param <T> a type
	 * @return a hashtable
	 */
	public <T> Hashtable<Class, Hashtable> makeTableIndex() {
		Hashtable<Class, Hashtable> table = new Hashtable<>();
		table.put(Entity.class, entityTable);
		table.put(Activity.class, activityTable);

		return table;
	}

	final public Hashtable<String, List<Statement>> successfulMerge = new Hashtable<>();
	final public Hashtable<String, List<Statement>> failedMerge = new Hashtable<>();
	final public Hashtable<String, List<Statement>> qualifiedNameMismatch = new Hashtable<>();

	/*
	 * TODO: check if resolveDuplicate for entity/activity/agent can be made
	 * generic.
	 */

	public Entity resolveDuplicate(Entity e, Entity entry) {
		String uri = e.getId().getUri();
        List<Statement> l = successfulMerge.computeIfAbsent(uri, k -> new LinkedList<>());
        l.add(e);
		return p.addAttributes(e, entry);
	}

	private void showStacktrace() {
		try {
			throw new NullPointerException();
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		}
	}

	public Activity resolveDuplicate(Activity a, Activity entry) {
		String uri = a.getId().getUri();
		Activity isMerged = merger.merge(a, entry);
		if (isMerged!=null) {
            List<Statement> l = successfulMerge.computeIfAbsent(uri, k -> new LinkedList<>());
            l.add(a);
			return p.addAttributes(a, entry);
		} else {
			addToFailedMerge(a, entry, uri);
			return entry;
		}
	}



	public Agent resolveDuplicate(Agent ag, Agent entry) {
		String uri = ag.getId().getUri();
        List<Statement> l = successfulMerge.computeIfAbsent(uri, k -> new LinkedList<>());
        l.add(ag);
		return p.addAttributes(ag, entry);
	}

	public <T extends Influence> T resolveDuplicate(T fluen, T entry) {
		String uri = fluen.getId().getUri();
		T isMerged = merger.merge(fluen, entry);
		if (isMerged != null) {
			List<Statement> l = successfulMerge.get(uri);
			if (l == null) {
				l = new LinkedList<>();
				l.add(entry);
				successfulMerge.put(uri, l);
			}
			l.add(fluen);
			return addAttributes(fluen, entry);
		} else {
			addToFailedMerge(fluen, entry, uri);
			return entry;
		}
	}


	@SuppressWarnings("unchecked")
	public <T> T addAttributes(T from, T to) {
		if (from instanceof Used) {
			return (T) p.addAttributes((Used) from, (Used) to);
		}
		if (from instanceof WasStartedBy) {
			return (T) p.addAttributes((WasStartedBy) from, (WasStartedBy) to);
		}
		if (from instanceof WasEndedBy) {
			return (T) p.addAttributes((WasEndedBy) from, (WasEndedBy) to);
		}
		if (from instanceof WasGeneratedBy) {
			return (T) p.addAttributes((WasGeneratedBy) from,
					(WasGeneratedBy) to);
		}
		if (from instanceof WasDerivedFrom) {
			return (T) p.addAttributes((WasDerivedFrom) from,
					(WasDerivedFrom) to);
		}
		if (from instanceof WasAssociatedWith) {
			return (T) p.addAttributes((WasAssociatedWith) from,
					(WasAssociatedWith) to);
		}
		if (from instanceof WasInvalidatedBy) {
			return (T) p.addAttributes((WasInvalidatedBy) from,
					(WasInvalidatedBy) to);
		}

		if (from instanceof WasAttributedTo) {
			return (T) p.addAttributes((WasAttributedTo) from,
					(WasAttributedTo) to);
		}
		/*
		 * if (from instanceof WasRevisionOf) { return (T)
		 * of.addAttributes((WasRevisionOf)from, (WasRevisionOf)to); } if (from
		 * instanceof AlternateOf) { return (T)
		 * of.addAttributes((AlternateOf)from, (AlternateOf)to); } if (from
		 * instanceof SpecializationOf) { return (T)
		 * of.addAttributes((SpecializationOf)from, (SpecializationOf)to); }
		 */
		if (from instanceof WasInformedBy) {
			return (T) p.addAttributes((WasInformedBy) from,
					(WasInformedBy) to);
		}
		if (from instanceof WasInfluencedBy) {
			return (T) p.addAttributes((WasInfluencedBy) from,
					(WasInfluencedBy) to);
		}
		if (from instanceof ActedOnBehalfOf) {
			return (T) p.addAttributes((ActedOnBehalfOf) from,
					(ActedOnBehalfOf) to);
		}

		/*
		 * if (from instanceof DerivedByInsertionFrom) { return T
		 * of.addAttributes((DerivedByInsertionFrom)from,
		 * (DerivedByInsertionFrom)to); }
		 */
		System.out.println("addAttributes Unknown relation " + from);
		throw new UnsupportedOperationException();
	}


	public MentionOf resolveDuplicate(MentionOf men, MentionOf entry) {
		String uri = men.getSpecificEntity().getUri();
		MentionOf isMerged = merger.merge(men, entry);
		if (isMerged != null) {
			List<Statement> l = successfulMerge.get(uri);
			if (l == null) {
				l = new LinkedList<>();
				l.add(entry);
				successfulMerge.put(uri, l);
			}
			l.add(men);
			return entry;
		} else {
			addToFailedMerge(men, entry, uri);
			return entry;
		}
	}

	public void addToFailedMerge(Activity a, Activity entry, String uri) {
		List<Statement> l = failedMerge.get(uri);
		if (l == null) {
			l = new LinkedList<>();
			l.add(entry);
			failedMerge.put(uri, l);
		}
		l.add(a);
	}

	public void addToFailedMerge(MentionOf a, MentionOf entry, String uri) {
		List<Statement> l = failedMerge.get(uri);
		if (l == null) {
			l = new LinkedList<>();
			l.add(entry);
			failedMerge.put(uri, l);
		}
		l.add(a);
	}

	public void addToFailedMerge(Activity a, Statement entry, String uri) {
		List<Statement> l = failedMerge.get(uri);
		if (l == null) {
			l = new LinkedList<>();
			l.add(entry);
			failedMerge.put(uri, l);
		}
		l.add(a);
	}

	public <T extends Influence> void addToFailedMerge(T fluen, T entry,
													   String uri) {
		List<Statement> l = failedMerge.get(uri);
		if (l == null) {
			l = new LinkedList<>();
			l.add(entry);
			failedMerge.put(uri, l);
		}
		l.add(fluen);
	}

}
