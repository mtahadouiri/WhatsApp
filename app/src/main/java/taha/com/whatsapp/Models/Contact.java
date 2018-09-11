package taha.com.whatsapp.Models;

/**
 * Created by Taha on 10/09/2018.
 */

public class Contact {
    private String nom;
    private String number;
    private String imageURL;

    public Contact(String nom, String number, String imageURL) {
        this.nom = nom;
        this.number = number;
        this.imageURL = imageURL;
    }

    public Contact() {
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "nom='" + nom + '\'' +
                ", number='" + number + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}
