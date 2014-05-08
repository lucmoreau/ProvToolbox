package org.openprovenance.prov.generator;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.HasOther;
import org.openprovenance.prov.model.Identifiable;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.StatementOrBundle;

/** A random  generator of PROV Graph.
    Initially designed by Jamal A. Hussein jah1g12@ecs.soton.ac.uk.
    Adapted to ProvToolbox by Luc Moreau */


public class GraphGenerator {
	
	public static final String TERM_PREFIX = "term";
	public static final String TERM_NS = "http://openprovenance.org/term/";
	public static final String FIRST_NODE_AS_ACTIVITY = "activity";
	public static final String FIRST_NODE_AS_ENTITY = "entity";

	private static int NO_OF_EDGES = 4;
	
	private Tree<String> graph = null;
	private LinkedList<String> list = null;
	private int noOfNodes = 10;
	private int noOfEdges = NO_OF_EDGES;
	private int count = 1;

	private String root = "e1";
	private final int ENTITY = 1;
	private final int ACTIVITY = 2;

	private final int AGENT = 3;

	final private ProvFactory pf;
	
	final private Random randomGenerator;
	
	final private long seed;
	final private String namespace;
	private String term;

	public GraphGenerator(int noOfNodes, int noOfEdges, String firstNode, String namespace, ProvFactory pf) {
		this(noOfNodes, noOfEdges, firstNode, namespace, pf, null, null);
	}

	public GraphGenerator(int noOfNodes, int noOfEdges, String firstNode, String namespace, ProvFactory pf, Long seed, String term) {
		if (FIRST_NODE_AS_ACTIVITY.equals(firstNode)) {
			this.root = "a1";
		}
		this.graph = new Tree<String>(this.root);
		this.list = new LinkedList<String>();
		this.noOfNodes = noOfNodes;
		this.noOfEdges = noOfEdges;
		this.namespace=namespace;
		this.pf=pf;
		if (seed==null) {
			this.seed=new Random().nextLong();
			this.randomGenerator = new Random(this.seed);
		} else {
			this.seed=seed;
			this.randomGenerator = new Random(seed);
		}
		System.out.println("GraphGenerator seed: " + this.seed);
		this.term=term;
	}

	private void addElement(String root_, String node) {
		graph.addLeaf(root_, node);
		list.add(node);
	}
	
	public long getSeed () {
		return seed;
	}

	public void generateElements() {
		String node = "";
		int n;
		int nd = ENTITY;
		list.add(this.root);
		while (!list.isEmpty()) {
			String root_ = list.pollFirst();
			if (root_.startsWith("e") || root_.startsWith("a")) {
				n = randomGenerator.nextInt(noOfEdges) + 1;
				for (int i = 0; i < n; i++) {
					switch (nd) {
					case ENTITY:
						count++;
						node = "e" + count;
						addElement(root_, node);
						nd = ACTIVITY;
						break;
					case ACTIVITY:
						count++;
						node = "a" + count;
						addElement(root_, node);
						nd = AGENT;
						if(count%3==0)
						 nd = ENTITY;
						break;
					case AGENT:
						count++;
						n = randomGenerator.nextInt(2);
						if (n == 1) {
							node = "g" + count;
						} else {
							node = "e" + count;
						}
						addElement(root_, node);						
						nd = 0;
						break;
					case 0:
					        if (list.size()>0) { //added by Luc
					            int nn = list.size()
					        	    - randomGenerator.nextInt(list.size());
					            if (nn < list.size()) {
					        	node = "*" + list.get(nn);
					            } else
					        	node = "*" + list.peek();
					            Collection<String> c = graph.getSuccessors(root_);
					            if (!c.contains(node.substring(1)))
					        	graph.addLeaf(root_, node);
					            nd = ENTITY;
					        }
						break;
					}
				}

			} else if (root_.startsWith("g")) {
				n = randomGenerator.nextInt(3);
				if (n == 1) {
					count++;
					node = "g" + count;
					addElement(root_, node);
				}
			}
			if (count >= noOfNodes)
				break;
		}
	}

	public int getCount() {
		return count;
	}

	private void getElements(String root, Tree<String> graph, Namespace ns, List<Statement> ll) {
		
		if (!root.startsWith("*")) {
			if (root.startsWith("e")) {
				ll.add(pf.newEntity(ns.stringToQualifiedName(root, pf)));
			} else if (root.startsWith("a")) {
				ll.add(pf.newActivity(ns.stringToQualifiedName(root, pf)));
			} else if (root.startsWith("g")) {
				ll.add(pf.newAgent(ns.stringToQualifiedName(root, pf)));
			}
		}

		Collection<String> sucessors = graph.getSuccessors(root);
		if (!sucessors.isEmpty()) {
			Iterator<String> iterator = sucessors.iterator();
			while (iterator.hasNext()) {
				String node = iterator.next();
				getElements(node, graph, ns, ll);
				if (root.startsWith("e")) {
					if (node.startsWith("e")) {
						ll.add(pf.newWasDerivedFrom(ns.stringToQualifiedName(root, pf),
								ns.stringToQualifiedName(node, pf)));
					} else if (node.startsWith("a")) {
						ll.add(pf.newWasGeneratedBy(null, ns.stringToQualifiedName(root, pf),
								ns.stringToQualifiedName(node, pf)));
					} else if (node.startsWith("g")) {
						ll.add(pf.newWasAttributedTo(null, ns.stringToQualifiedName(root, pf), 
								ns.stringToQualifiedName(node, pf)));						
					} else if (node.startsWith("*")) {
						String node1 = node.substring(1);
						if (node1.startsWith("e")) {
							ll.add(pf.newWasDerivedFrom(ns.stringToQualifiedName(root, pf),
									ns.stringToQualifiedName(node1, pf)));
						} else if (node1.startsWith("a")) {
							ll.add(pf.newWasGeneratedBy(null, ns.stringToQualifiedName(root, pf),
									ns.stringToQualifiedName(node1, pf)));
						} else if (node1.startsWith("g")) {
							ll.add(pf.newWasAttributedTo(null, ns.stringToQualifiedName(root, pf), 
									ns.stringToQualifiedName(node1, pf)));
						}
					}
				} else if (root.startsWith("a")) {
					if (node.startsWith("e")) {
						ll.add(pf.newUsed(ns.stringToQualifiedName(root, pf), 
								ns.stringToQualifiedName(node, pf)));
					} else if (node.startsWith("a")) {
						ll.add(pf.newWasInformedBy(null,ns.stringToQualifiedName(root, pf), 
								ns.stringToQualifiedName(node, pf)));
					} else if (node.startsWith("g")) {
						ll.add(pf.newWasAssociatedWith(null,ns.stringToQualifiedName(root, pf), 
								ns.stringToQualifiedName(node, pf)));
					} else if (node.startsWith("*")) {
						String node1 = node.substring(1);
						if (node1.startsWith("e")) {
							ll.add(pf.newUsed(ns.stringToQualifiedName(root, pf), 
									ns.stringToQualifiedName(node1, pf)));
						} else if (node1.startsWith("a")) {
							ll.add(pf.newWasInformedBy(null,ns.stringToQualifiedName(root, pf), 
									ns.stringToQualifiedName(node1, pf)));
						} else if (node1.startsWith("g")) {
							ll.add(pf.newWasAssociatedWith(null,ns.stringToQualifiedName(root, pf), 
									ns.stringToQualifiedName(node1, pf)));
						}
					}
				} else if (root.startsWith("g")) {
					if (node.startsWith("g")) {
						ll.add(pf.newActedOnBehalfOf(null,ns.stringToQualifiedName(root, pf), 
								ns.stringToQualifiedName(node, pf)));
					}
				}
			}
		}
	}

	public Document getDocument() {
		root = graph.getHead();

		Document doc=pf.newDocument();
		Namespace ns=new Namespace();
		ns.setDefaultNamespace(namespace);
		
		List<Statement> statements=new LinkedList<Statement>();

		getElements(root, graph, ns, statements);
		doc.getStatementOrBundle().addAll(statements);
		doc.setNamespace(ns);
		if (term!=null) {
			addSeed(doc,seed,term);
		}
		return doc;
	}
	
	public void addSeed(Document doc, long seed, String name) {
		Namespace namespace = doc.getNamespace();
		QualifiedName qn = namespace.stringToQualifiedName(name, pf);
		for (StatementOrBundle statement: doc.getStatementOrBundle()) {
			if (statement instanceof Identifiable) {
				Identifiable ss=(Identifiable) statement;			
				if ((ss.getId()!=null) && (ss.getId().equals(qn))) {
					namespace.register(TERM_PREFIX, TERM_NS);
					namespace.addKnownNamespaces();
					((HasOther) ss).getOther().add(pf.newOther(TERM_NS, "seed", TERM_PREFIX, seed, pf.getName().XSD_LONG));
					return;
				}
			}
		}
		
	}
	
}
