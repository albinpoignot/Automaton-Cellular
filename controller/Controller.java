package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Timer;

import model.Grid;
import model.Person;
import view.Chart;
import view.ChartLine;
import view.DrawAreaUI;
import view.GridUI;
import view.PersonUI;

public class Controller {

	/**
	 * Probabilité de panique des personnes
	 */
	private final Double conflit = 0.5;
	private final Float temps = 0.4f;

	/**
	 * Aire de dessin
	 */
	private DrawAreaUI drawArea;

	/**
	 * Grille du système
	 */
	private Grid grid;

	/**
	 * Tableau des personnes actuellement dans le système
	 */
	private ArrayList<Person> personnes;

	/**
	 * Timer permettant le déplacement régulier
	 */
	private Timer timer;

	/**
	 * Diagramme d'affichage des dates de sortie
	 */
	private ChartLine chartExit;

	/**
	 * Diagramme des distances avant sortie
	 */
	private Chart chartDistance;

	/**
	 * Nombre d'itérations depuis le début du traitement
	 */
	private int nbIterations;

	/**
	 * Constructeur. Crée une nouvelle DrawAreaUI.
	 * Il initialise les propriétés privées et déclenche un timer qui se charge de
	 * redessiner la zone toute les secondes.
	 * @param lagrille : grille utilisée comme modèle 
	 * @param lesPersonnes : les personnes à diriger sur la grille
	 * @param chartline : le graphique affichant les temps d'évacuation
	 */
	public Controller(Grid lagrille,List<Integer[]> lesPersonnes, ChartLine chartline)
	{

		/*
		 *  STEP 1 : Définition de la grille
		 */
		drawArea = new DrawAreaUI();
		this.grid = lagrille;
		drawArea.setGridUi(new GridUI(grid));
		drawArea.paintGrid();

		/*
		 * STEP 2 : Définition des personnes
		 */
		this.personnes = new ArrayList<Person>();
		int i = 0;
		for(Integer[] p: lesPersonnes)
		{
			addPerson(new Person(i, p[0], p[1], this));
			i++;
		}

		/*
		 * STEP 3 : Définition des charts 
		 */
		this.chartExit = chartline;
		nbIterations = 0;

		chartDistance = new Chart("Distance parcourue");

		/*
		 * STEP 4 : Définition du timer (toute les 1 secondes)
		 */
		timer = new Timer( 1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				ArrayList<Person> clonePersonnes = new ArrayList<Person>();
				for (Person p : personnes )
				{
					clonePersonnes.add((Person) p.clone());
				}				

				Iterator<Person> it = clonePersonnes.iterator();
				Iterator<Person> it2 = personnes.iterator();

				ArrayList<Person> personToRemove = new ArrayList<Person>();

				Person p;
				Person p2;
				while(it.hasNext())
				{
					p = it.next();
					p2 = it2.next();

					// Dans le cas où la personne n'est plus dans le système
					if(!p.updatePosition()) 
					{
						personToRemove.add(p2);
						it.remove();	

						chartDistance.addPoint(p.getDistance(), "Distances", Integer.toString(p.getId()));

						drawArea.removePersonUi(p.getUi());					
					}
				}
				Iterator<Person> it3 = personnes.iterator();
				while(it3.hasNext())
				{
					p2 = it3.next();
					if(personToRemove.contains(p2)){
						it3.remove();
					}
				}
				personToRemove.clear();
				resolveConflicts(clonePersonnes);

				for(int i = 0; i < clonePersonnes.size(); i++)
				{
					personnes.get(i).update(clonePersonnes.get(i));
				}
				if(personnes.isEmpty())
				{
					// en 1 iteration, l'indivitdu se déplace de 0.4m à 1m/s
					// il s'écoule donc 0.4 seconde par itération 
					System.out.println("Temps d'évacuation:" +nbIterations * temps + " s");
					chartExit.addPoint(nbIterations * temps, "Dates de sortie", Integer.toString(grid.getNbExit()));
					stopSimulation();
				}
				drawArea.updateItems();
				nbIterations++;
			}
		} );
		timer.setInitialDelay(1000);
	}

	/**
	 * Ajoute une Person à la Grille gérée par ce Controlleur
	 * @param person La personne à ajouter
	 */
	public void addPerson(Person person)
	{
		//person.setGrille(grille);
		PersonUI pUi = new PersonUI();
		pUi.setPerson(person);
		pUi.setGridUI(drawArea.getGridUi());
		person.setUi(pUi);

		personnes.add(person);
		drawArea.addPersonUi(pUi);	
	}

	/**
	 * Retourne l'aire de dessin gérée par ce controleur
	 * @return l'aire de dessin gérée par ce controleur
	 */
	public DrawAreaUI getDrawArea() {
		return drawArea;
	}

	/**
	 * Retourne la grille gérée par ce controleur
	 * @return la grille gérée par ce controleur
	 */
	public Grid getGrid() {
		return grid;
	}

	/**
	 * Retourne la position d'une personne dans la liste des personnes
	 * @param p La personne dont on cherche la position
	 * @return La position de la personne dans la liste des personnes
	 */
	public int getId(Person p)
	{
		return personnes.indexOf(p);
	}

	/**
	 * Indique si une position est occupée ou non
	 * @param pos La position à vérifier
	 * @return True si la position est occupée, False sinon
	 */
	public Boolean isOccupied(Integer[] pos)
	{	
		for (Person p : personnes) {
			if(p.getLine() == pos[0] && p.getColonne() == pos[1] && p.getId() != pos[2])
			{				
				return true; 
			}
		}
		return false;
	}

	/**
	 * Si 2 personnes parmis la nouvelle grille ont fait le même choix de case, une personne est tirée au sort
	 * pour rester sur sa case tandis que l'autre reprend sa position précédente
	 * @param nextPersonnes
	 */
	private void resolveConflicts(ArrayList<Person> nextPersonnes)
	{
		int i = 0;

		for (Person p : nextPersonnes)
		{
			int j = 0;
			for (Person p2 : nextPersonnes)
			{
				if(p.getLine() == p2.getLine()  && p.getColonne() == p2.getColonne() && p.getId() != p2.getId())
				{
					//System.out.println("conflit");					
					if(conflit.compareTo(Math.random()) < 0 )
					{
						p.setLine(personnes.get(i).getLine());
						p.setColumn(personnes.get(i).getColonne());
					}
					else
					{
						p2.setLine(personnes.get(j).getLine());
						p2.setColumn(personnes.get(j).getColonne());
					}
				}
				j++;
			}
			i++;
		}

	}

	/**
	 * Définit la zone de dessin à rattacher à ce controleur
	 * @param drawArea La zone de dessin à rattacher 
	 */
	public void setDrawArea(DrawAreaUI drawArea) {
		this.drawArea = drawArea;
	}

	/**
	 * Démarre la simulation
	 */
	public void startSimulation()
	{
		timer.start();
	}

	/**
	 * Arrête la simulation
	 */
	public void stopSimulation()
	{
		timer.stop();
	}

}
