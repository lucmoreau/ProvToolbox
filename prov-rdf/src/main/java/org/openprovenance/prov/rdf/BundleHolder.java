package org.openprovenance.prov.rdf;

import java.util.ArrayList;
import java.util.List;

public class BundleHolder {
	private List<org.openprovenance.prov.xml.Activity> activities;
	private List<org.openprovenance.prov.xml.Agent> agents;
	private List<org.openprovenance.prov.xml.Entity> entities;
	private List<org.openprovenance.prov.xml.Statement> statements;

	public BundleHolder()
	{
		this.activities = new ArrayList<org.openprovenance.prov.xml.Activity>();
		this.agents = new ArrayList<org.openprovenance.prov.xml.Agent>();
		this.entities = new ArrayList<org.openprovenance.prov.xml.Entity>();
		this.statements = new ArrayList<org.openprovenance.prov.xml.Statement>();
	}

	public List<org.openprovenance.prov.xml.Activity> getActivities()
	{
		return activities;
	}

	public void setActivities(
			List<org.openprovenance.prov.xml.Activity> activities)
	{
		this.activities = activities;
	}

	public List<org.openprovenance.prov.xml.Agent> getAgents()
	{
		return agents;
	}

	public void setAgents(List<org.openprovenance.prov.xml.Agent> agents)
	{
		this.agents = agents;
	}

	public List<org.openprovenance.prov.xml.Entity> getEntities()
	{
		return entities;
	}

	public void setEntities(List<org.openprovenance.prov.xml.Entity> entities)
	{
		this.entities = entities;
	}

	public List<org.openprovenance.prov.xml.Statement> getStatements()
	{
		return statements;
	}

	public void setStatements(
			List<org.openprovenance.prov.xml.Statement> statements)
	{
		this.statements = statements;
	}
	
	public void addAgent(org.openprovenance.prov.xml.Agent agent) {
		this.agents.add(agent);
	}
	
	public void addActivity(org.openprovenance.prov.xml.Activity activity) {
		this.activities.add(activity);
	}
	
	public void addEntity(org.openprovenance.prov.xml.Entity entity) {
		this.entities.add(entity);
	}
	
	public void addStatement(org.openprovenance.prov.xml.Statement statement) {
		this.statements.add(statement);
	}

}
