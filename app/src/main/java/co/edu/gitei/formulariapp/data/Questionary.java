package co.edu.gitei.formulariapp.data;

/**
 * Created by CONTENIDOS on 16/11/2017.
 */

public class Questionary {
    private long id;
    private String formReference;
    private String answers;

    public Questionary(long id, String formReference, String answers) {
        this.id=id;
        this.formReference=formReference;
        this.answers=answers;
    }

    public Questionary() {

    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFormReference() {
        return formReference;
    }

    public void setFormReference(String formReference) {
        this.formReference = formReference;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }
}
