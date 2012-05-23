/**
 * 
 */
package model;

import java.sql.Date;
import java.util.Calendar;
import java.util.Random;

/**
 * @author albin
 *
 */
public class Neighborhood {
	
	/**
	 * Nombre de lignes d'un voisinage
	 */
	public static final Integer NB_LIGNES = 3;
	
	/**
	 * Nombre de colonnes d'un voisinage
	 */
	public static final Integer NB_COLONNES = 3;
	
	/**
	 * Position en x de la case à valeur minimum de ce voisinage
	 */
	private int miniX;
	
	/**
	 * Position en y de la case à valeur minimum de ce voisinage
	 */
	private int miniY;
	
	/**
	 * La personne dont on considère le voisinage
	 */
	private Person person;
	
	/**
	 * Le voisinage
	 */
	private Float[][] voisinage;
	
	/**
	 * Constructeur. Ce constructeur construit le voisinage en fonction de la grille
	 * et de la personne passés en paramètres.
	 * @param person La personne dont on cherche le voisinage
	 */
	public Neighborhood(Person person)
	{
		this.person = person;
		construireVoisinage(person.getGrille());
	}
	
	/**
	 * Affiche le voisinage dans la console
	 */
	public void afficherNeighborhood()
	{
		int ligne,colonne;
		
		for(ligne=0; ligne < NB_LIGNES; ligne++)
		//for(ligne=NB_LIGNES-1; ligne >= 0 ; ligne--)
		{
			for(colonne=0; colonne < NB_COLONNES; colonne++)
			//for(colonne=NB_COLONNES-1; colonne >= 0 ; colonne--)
			{
				System.out.print(voisinage[ligne][colonne]);
				System.out.print("     ");
			}
			System.out.println();
		}
	}
	
	/**
	 * Construit le voisinage en fonction de l'attribut person
	 * @param grille La grille dans laquelle on recherche le voisinage
	 */
	public void construireVoisinage(Grille grille)
	{
		int ligne,colonne;
		voisinage = new Float[NB_LIGNES][NB_COLONNES];
		miniX = 0;
		miniY = 0;
		
		for(ligne=0; ligne < NB_LIGNES; ligne++)
		{
			for(colonne=0; colonne < NB_COLONNES; colonne++)
			{
				voisinage[ligne][colonne] = 
						grille.getValue(person.getLigne() + (ligne - 1), person.getColonne() + (colonne - 1));
								
				if(voisinage[ligne][colonne].compareTo(voisinage[miniX][miniY]) < 0)
				{
					miniX = ligne;
					miniY = colonne;
				}
				else if(voisinage[ligne][colonne].compareTo(voisinage[miniX][miniY]) == 0)
				{
					double rnd = Math.random();
					
					if(rnd > 0.5)
					{
						miniX = ligne;
						miniY = colonne;
					}
				}
				
			}			
		}
	}
	
	/**
	 * Retourne la position minimum de ce voisinage
	 * @return la position minimum de ce voisinage dans un tableau ayant cette forme :
	 * 	[0] : position en x
	 * 	[1] : position en y
	 */
	public int[] getMiniPosition()
	{
		int[] pos = new int[2];
		pos[0] = miniX;
		pos[1] = miniY;
		return pos;
	}
	
	/**
	 * @return la personne concernée par ce voisinage
	 */
	public Person getPerson()
	{
		return person;
	}
	
	/**
	 * Retourne la distance de la sortie de la case (x,y)
	 * @param x Coordonnée en x de la case
	 * @param y Coordonnée en y de la case
	 * @return La distance de la sortie par rapport à la case (x,y) ou -1 si la case n'existe pas
	 */
	public Float getValue(Integer x, Integer y)
	{
		Float retour = -1.0f;
		
		if(x <= NB_LIGNES && y <= NB_COLONNES)
		{
			retour = voisinage[x][y];
		}
		
		return retour;
		
	}
	

	/**
	 * @return le voisinage
	 */
	public Float[][] getVoisinage()
	{
		return voisinage;
	}

}
