import java.util.Vector;

/**
 * Created by Julien on 1/9/2016.
 */
public class Sparse_Table <E> {


    //Ma classe BitMap n'est rien d'autre qu'un entier long
    // On a besoin théoriquement d'un tableau de Bit (de booleens en fait) , donc avec les bons
    // getteurs et setteurs, en utilisant les opérateurs booleens, un int suffit largement et sauve donc de la place

    //En fait, il s'agit d'une version simplifiee de la classe BitSet de Java, qui pourrait fonctionner
    // Ici, on est limite du coup a 64-1 bits (bit de positivite) soit M = 62 BitSet n'a pas de limite de Bit.
    // En toute connaissance de cause, je vais faire comme si BitSet n'existe pas, et le faire à la main avec BitMap
    private class BitMap{
        public long value;

        private BitMap(){
            this.value=0;
        }

        // met l'indice index a 1 si bool, 0 sinon
        private void set (int index, boolean bool){
            if (bool){
                value|= (long) (Math.pow(2,index));
                return;
            }//else
            value&= ~(long) (Math.pow(2,index));
        }

        //si le resultat est non nul, c'est que le bit est a 1, sinon 0
        private boolean get(int index){
            return((value & (long) (Math.pow(2,index)))!=0);
        }

    }


    private class Groupe{

        private Vector values;
        //Vector of bits. Choix de cette strucure apres lecture de la doc, qui s'adapte bien a notre probleme
        private BitMap bitmaps;
        private int M;

        // initialisation du groupe avec le nb de collisions autorisees
        private Groupe(int nbcoll){
            this.M=nbcoll;
            this.values = new Vector();
            this.bitmaps = new BitMap();
        }

        //cette fonction donne quel est le prochain indice qui peut etre utilise si index n'a pas ete utilise
        //Si index a ete utilise, alors cela retourne l'indice i a lequel il est associé, +1 !
        private int whichIndex(int index){
            int res=0;
            for(int i=0;i<=index;i++){
                if(bitmaps.get(i)){
                    res+=1;
                }
            }
            return res;
        }

        //cette fonction verifie si un index a deja ete place
        private boolean lookup(int index){
            return (bitmaps.get(index));
        }

        // fonction qui place la valeur put pour la cle key
        private void put(int key,E value){
            //on calcule l'indice dans lequel il sera stocke
            int index = key % M;
            int whichindex=whichIndex(index);
            //si on a deja reference cette cle
            if (lookup(index)){
                //on remplace
                values.setElementAt(value,whichindex-1);
                return;
            }
            //sinon on marque la place prise et on le place
            bitmaps.set(index,true);
            values.add(whichindex,value);
        }

        //Fonction qui va donner la valeur assignee a key
        private E get(int key){
            int index = key % M;
            // si elle y est
            if (lookup(index)){
                return (E) values.elementAt(whichIndex(index)-1);
            }
            //sinon je renvoie null
            return null;
        }


        //delete la valeur associee a key
        private void delete(int key) {
            int index = key % M;
            // si la key existe
            if (lookup(index)){
                values.removeElementAt(whichIndex(index)-1);
                //on n'oublie pas de desallouer la place
                bitmaps.set(index,false);
            }
            //sinon il ne se passe rien

        }
        // Cela fait en fait la somme de tous les bits assignes a 1
        private int size() {
            return whichIndex(M);
        }
    }


    private Vector<Groupe> table; //table de groupes(bitmaps,vecteur de valeurs)
    private int M ; // nombre de collisions maximales autorisées
    private int keymax;


    //initialise le tableau selon la taille donee et la valeur de M
    private void init_table(int taille){
        this.keymax=taille-1;
        this.table = new Vector();
        for (int i=0;i<(keymax/M)+1;i++){
            table.add(new Groupe(M));
        }
    }

    //cas ou M n'est pas donnee en argument
    public Sparse_Table (int taille){
        this.M=48; //valeur par defaut si non donnee
        this.init_table(taille);
    }


    //je choisis de donner en argument la taille de la sparse table ainsi que le nombre maximal de collisions
    public Sparse_Table (int taille, int nbcollisions){
        //on va faire en sorte de ne jamais depasser M=62, valeur maximale pour mon choix de BitMap, theoriquement infinie pour Bitset
        // Si on depasse 62, on repasse alors a la valeur par defaut conseillee : 48
        if (nbcollisions>62){
            System.out.println("Too high number of collisions (>=63). Automatically set to 48");
            this.M=48;
            this.init_table(taille);
        }else{
            this.M=nbcollisions;
            this.init_table(taille);
        }
    }

    //ajoute key->value a la sparse table
    public void put(int key, E value){
        if(key>keymax){
            System.err.println("Warning : key out of bounds >=" +keymax);
            return;
        }
        int index = key/M;
        //je recupere le bon groupe, et je delegue a la fonction put des groupes
        table.elementAt(index).put(key,value);
    }

    //retourne une eventuelle valeur associee a key, sinon null
    public E get(int key){if(key>keymax){
        System.err.println("Warning : key out of bounds >=" +keymax);
        return null;
    }
        return table.elementAt(key/M).get(key);
    }

    //supprime la valeur associee a key, sinon ne fait rien
    public void delete(int key){
        if(key>keymax){
            System.err.println("Warning : key out of bounds >=" +keymax);
            return;
        }
        table.elementAt(key/M).delete(key);
    }

    //calcule la taille de la sparse table, c'est a dire le nombre d'index assignes
    public int size(){
        int res=0;
        for (int i=0;i<table.size(); i++){
            // je somme les index utilises pour chaque groupe
            //System.err.println( "Groupe " + i +" -> " + table.elementAt(i).size() );
            res+= table.elementAt(i).size();
        }
        return res;
    }



}
