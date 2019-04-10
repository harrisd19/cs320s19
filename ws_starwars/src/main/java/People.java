/*
 * CSCI320 - Networking & Distributed Computing - Spring 2019
 * Instructor: Thyago Mota
 * Description: Hwk 04 - People
 * Your name(s):
 */

import java.util.Arrays;

public class People {

    String name, height, mass, hair_color, skin_color, eye_color, birth_year, gender, homeworld, created, edited, url;
    String[] films, species, vehicles, starships;

    @Override
    public String toString() {
        return "People{" +
                "name='" + name + '\'' +
                ", height='" + height + '\'' +
                ", mass='" + mass + '\'' +
                ", hair_color='" + hair_color + '\'' +
                ", skin_color='" + skin_color + '\'' +
                ", eye_color='" + eye_color + '\'' +
                ", birth_year='" + birth_year + '\'' +
                ", gender='" + gender + '\'' +
                ", homeworld='" + homeworld + '\'' +
                ", created='" + created + '\'' +
                ", edited='" + edited + '\'' +
                ", url='" + url + '\'' +
                ", films=" + Arrays.toString(films) +
                ", species=" + Arrays.toString(species) +
                ", vehicles=" + Arrays.toString(vehicles) +
                ", starships=" + Arrays.toString(starships) +
                '}';
    }
}
