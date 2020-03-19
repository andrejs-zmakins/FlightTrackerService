package flights.flighttracker.airport;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Airport entity class.
 * @author Andrejs Zmakins
 */
@ApiModel(description="Details about the airport.")
@Entity(name = "Airport")
public class Airport {

    @Id
    @GeneratedValue
    private Integer id;

    @NotBlank
    @ApiModelProperty
    @Size(max=32)
    private String name;

    @Size(min=3, max=3, message="Should be exactly three characters.")
    @ApiModelProperty(notes="Should be exactly three characters.")
    private String iata;


    // Necessary for the framework.
    protected Airport() { }


    public Airport(int id, String name, String iata) {
        super();

        this.id = id;
        this.name = name;
        this.iata = iata;
    }


    public Airport(@NotBlank String name,
                   @Size(min = 3, max = 3, message = "Should be exactly three characters.") String iata) {
        super();

        this.name = name;
        this.iata = iata;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIata() {
        return iata;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }

    @Override
    public String toString() {
        return "Airport{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", iata='" + iata + '\'' +
                '}';
    }
}
