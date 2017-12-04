package co.edu.gitei.formulariapp.data;

/**
 * Created by CONTENIDOS on 16/11/2017.
 */

public class Questionary {
    private long id;
    private String formReference;
    private String formCreationIdentifier;
    private String answers;

    public Questionary(long id, String formReference, String formCreationIdentifier, String answers) {
        this.id=id;
        this.formCreationIdentifier=formCreationIdentifier;
        this.formReference=formReference;
        this.answers=answers;
    }

    public Questionary() {

    }

    public Questionary(String formRef, String formCreationId, String answers) {
        this.formReference=formRef;
        this.formCreationIdentifier=formCreationId;
        this.answers=answers;
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

    public String getFormCreationIdentifier() {
        return formCreationIdentifier;
    }

    public void setFormCreationIdentifier(String formCreationIdentifier) {
        this.formCreationIdentifier = formCreationIdentifier;
    }

    @Override
    public String toString(){
        String string=" Grupo de preguntas: "+getFormReference()+"\n Formulario: "+getFormCreationIdentifier()+"\n";
        string=string+" Respuestas: \n"+((getAnswers().replaceAll("[{]","")).replaceAll("[}]","\n")).replaceAll("\",","\"\n");

        return string;
    }
}
