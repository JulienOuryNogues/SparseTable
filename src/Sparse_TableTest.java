import org.junit.Test;

/**
 * Created by Julien on 1/16/2016.
 */
public class Sparse_TableTest {

    @Test
    //On va juste tester que les put ne plantent pas ici.
    public void testPut() throws Exception {
        Sparse_Table Vec = new Sparse_Table(100);
        Vec.put(2,"coucou");
        Vec.put(2,"Hello");
        Vec.put(0,"World");
        Vec.put(1,"!");

    }

    @Test
    // on va donc tester que les get rendent bien ce qu'il faut
    public void testGet() throws Exception {
        Sparse_Table Vec = new Sparse_Table(100);
        //on n'a encore rien mis
        assert(Vec.get(0)==null);
        Vec.put(2,"Depinfo");
        //test que V[2] == Depinfo
        assert(Vec.get(2).equals("Depinfo"));
        // test que V[2] vaut "IS" , et que l'ancienne valeur a ete ecrasee
        Vec.put(2,"IS");
        assert(Vec.get(2).equals("IS"));
        Vec.put(0,"Test");
        Vec.put(1,"Mot pour rien");
        assert(Vec.get(0).equals("Test"));
        assert(Vec.get(1).equals("Mot pour rien"));
        //on verifie bien que mon get renvoie null dans les cas de non assignation
        assert(Vec.get(3)==null);
        //encore une reassignation
        Vec.put(2,"I&S");
        assert(Vec.get(2).equals("I&S"));
        Vec.put(100,"Ne fait rien");
        assert(Vec.get(100)==null);//rien n'a ete affecte


    }

    @Test
    // On va tester la fonction delete, en verifiant que les get sont bien desassignes
    public void testDelete() throws Exception {
        Sparse_Table Vec = new Sparse_Table(100);
        Vec.put(2,"Depinfo");
        Vec.put(0,"IS");
        Vec.put(1,"IM");
        Vec.delete(2);
        //OK apres une suppression
        assert(Vec.get(2)==null);
        Vec.delete(4); //ne doit rien faire (jamasi assigne)
        assert(Vec.get(4)==null);
        //on verifie que cela n'a pas efface d'autre indices
        assert(Vec.get(0).equals("IS"));
        assert(Vec.get(1).equals("IM"));
        //on verifie qu'on peut reecrire par dessus un element efface
        Vec.put(2,"Best Dep");
        assert(Vec.get(2).equals("Best Dep"));
        //et on efface
        Vec.delete(2);
        assert(Vec.get(2)==null);
        //appelle juste un message d'erreur
        Vec.delete(100);

    }

    @Test
    public void testSize() throws Exception {
        //la Sparse Table se reset a 48 dans ce cas
        Sparse_Table Vec = new Sparse_Table(100,63);
        //au debut la table est vide
        assert(Vec.size()==0);
        Vec.put(2,"Taille 1");
        //test que V[2] a ete assigne -> taille 1
        assert(Vec.size()==1);
        // test que l'ancienne valeur a ete ecrasee
        Vec.put(2,"Toujours Taille 1");
        assert(Vec.size()==1);
        Vec.put(0,"Taille 2");
        Vec.put(1,"Et 3 !");
        // on verifie que le size marche apres plusieurs put
        assert(Vec.size()==3);
        Vec.delete(0);
        //apres un vrai delete
        assert(Vec.size()==2);
        //apres un faux delete
        Vec.delete(0);
        assert(Vec.size()==2);
        Vec.delete(1);
        assert(Vec.size()==1);

    }
}