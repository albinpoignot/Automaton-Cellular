/**
 * 
 */
package model;

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
	 * Position en x de la case a valeur minimum de ce voisinage
	 */
	private int miniX;

	/**
	 * Position en y de la case a valeur minimum de ce voisinage
	 */
	private int miniY;

	/**
	 * La personne dont on considere le voisinage
	 */
	private Person person;

	/**
	 * Le voisinage
	 */
	private Float[][] voisinage;

	/**
	 * Constructeur. Ce constructeur construit le voisinage en fonction de la grille
	 * et de la personne passes en parametres.
	 * @param person La personne dont on cherche le voisinage
	 */
	public Neighborhood(Person person)
	{
		this.person = person;
		buildNeighborhood(person.getGrid());
	}

	/**
	 * Affiche le voisinage dans la console
	 */
	public void showNeighborhood()
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
	public void buildNeighborhood(Grid grille)
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
						grille.getValue(person.getLine() + (ligne - 1), person.getColonne() + (colonne - 1));

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
	public int[] getMinPosition()
	{
		int[] pos = new int[2];
		pos[0] = miniX;
		pos[1] = miniY;
		return pos;
	}

	/**
	 * @return la personne concernee par ce voisinage
	 */
	public Person getPerson()
	{
		return person;
	}

	/**
	 * Retourne la distance de la sortie de la case (x,y)
	 * @param x Coordonnee en x de la case
	 * @param y Coordonnee en y de la case
	 * @return La distance de la sortie par rapport a la case (x,y) ou -1 si la case n'existe pas
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
	public Float[][] getNeighborhood()
	{
		return voisinage;
	}

}
