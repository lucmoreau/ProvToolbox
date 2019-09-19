package org.openprovenance.prov.rdf.collector;

import java.util.ArrayList;
import java.util.List;


public class BundleHolder {
	private List<org.openprovenance.prov.model.Activity> activities;
	private List<org.openprovenance.prov.model.Agent> agents;
	private List<org.openprovenance.prov.model.Entity> entities;
	private List<org.openprovenance.prov.model.Statement> statements;

	public BundleHolder()
	{
		this.activities = new ArrayList<org.openprovenance.prov.model.Activity>();
		this.agents = new ArrayList<org.openprovenance.prov.model.Agent>();
		this.entities = new ArrayList<org.openprovenance.prov.model.Entity>();
		this.statements = new ArrayList<org.openprovenance.prov.model.Statement>();
	}

	public List<org.openprovenance.prov.model.Activity> getActivities()
	{
		return activities;
	}

	public void setActivities(
			List<org.openprovenance.prov.model.Activity> activities)
	{
		this.activities = activities;
	}

	public List<org.openprovenance.prov.model.Agent> getAgents()
	{
		return agents;
	}

	public void setAgents(List<org.openprovenance.prov.model.Agent> agents)
	{
		this.agents = agents;
	}

	public List<org.openprovenance.prov.model.Entity> getEntities()
	{
		return entities;
	}

	public void setEntities(List<org.openprovenance.prov.model.Entity> entities)
	{
		this.entities = entities;
	}

	public List<org.openprovenance.prov.model.Statement> getStatements()
	{
		return statements;
	}

	public void setStatements(
			List<org.openprovenance.prov.model.Statement> statements)
	{
		this.statements = statements;
	}

	public void addAgent(org.openprovenance.prov.model.Agent agent)
	{
		if (!this.agents.contains(agent))
		{
			this.agents.add(agent);
		}
	}

	public void addActivity(org.openprovenance.prov.model.Activity activity)
	{
		if (!this.activities.contains(activity))
		{
			this.activities.add(activity);
		}
	}

	public void addEntity(org.openprovenance.prov.model.Entity entity)
	{
		if (!this.entities.contains(entity))
		{
			this.entities.add(entity);
		}
	}

	public void addStatement(org.openprovenance.prov.model.Statement statement)
	{
		if (!this.statements.contains(statement))
		{
			this.statements.add(statement);
		}
	}


	public void store(org.openprovenance.prov.model.Element element)
	{
		if (element instanceof org.openprovenance.prov.model.Activity)
		{
			this.addActivity(
					(org.openprovenance.prov.model.Activity) element);
		} else if (element instanceof org.openprovenance.prov.model.Entity)
		{

			this.addEntity(
					(org.openprovenance.prov.model.Entity) element);
		} else if (element instanceof org.openprovenance.prov.model.Agent)
		{

			this.addAgent(
					(org.openprovenance.prov.model.Agent) element);
		}
	}
}
