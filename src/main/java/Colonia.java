import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Colonia {
    @Id @GeneratedValue
    private long id;
    
    @Column(unique = true) // El nombre de la colonia es único [cite: 11]
    private String nombre;
    private String codigoPostal;

    @OneToMany(mappedBy = "colonia", cascade = CascadeType.PERSIST)
    private List<Vivienda> viviendas;

    public Colonia() {
        this.viviendas = new ArrayList<>();
    }

    public Colonia(String nombre, String codigoPostal) {
        this();
        this.nombre = nombre;
        this.codigoPostal = codigoPostal;
    }

    // Getters y Setters...
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public List<Vivienda> getViviendas() { return viviendas; }
}