package com.example.vivienda.model;

import javax.persistence.*;
import java.util.List;

public class SistemaCatastroDB {

    public static void main(String[] args) {
        // 1. Crear EntityManagerFactory para conectar a la BD
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vivienda-db");
        EntityManager em = emf.createEntityManager();

        try {
            // --- INICIAR TRANSACCIÓN PARA GUARDAR DATOS ---
            em.getTransaction().begin();

            // 2. Crear y persistir objetos
            Persona juanPerez = new Persona("Juan", "PEPJ800101", true, 65,"perez");
            Persona anaGomez = new Persona("Ana", "GOMA900202", false,18,"gomez");
            Colonia centro = new Colonia("Centro", "42000");
            CasaUnifamiliar casaJuan = new CasaUnifamiliar("Calle Hidalgo 10", 150.5, "CAT001", juanPerez, centro, 2);

            // Crear un Edificio de ejemplo
            Edificio edificioEjemplo = new Edificio("Av. Reforma 123", 800.0, "EDIF001", juanPerez, centro, 10);

            // 3. Establecer relaciones bidireccionales
            casaJuan.agregarHabitante(juanPerez);
            casaJuan.agregarHabitante(anaGomez);

            // 4. Persistir los objetos principales (ObjectDB guarda los relacionados por cascada)
            em.persist(juanPerez);
            em.persist(anaGomez);
            em.persist(centro);
            em.persist(casaJuan);
            em.persist(edificioEjemplo);

            // 5. Confirmar la transacción
            em.getTransaction().commit();
            System.out.println("Datos guardados exitosamente.");

            // --- CONSULTAR DATOS ---
            System.out.println("\n--- Consultando Viviendas en la Colonia Centro ---");
            TypedQuery<Vivienda> query = em.createQuery(
                    "SELECT v FROM Vivienda v WHERE v.colonia.nombre = 'Centro'", Vivienda.class);
            List<Vivienda> resultados = query.getResultList();

            for (Vivienda v : resultados) {
                System.out.println("Dirección: " + v.getDireccion());
                System.out.println("Propietario: " + v.getPropietario().getNombre());
                System.out.println("Habitantes: " + v.getHabitantes().size());
            }

            // --- CONSULTAR EDIFICIOS ---
            System.out.println("\n--- Consultando Edificios ---");
            TypedQuery<Edificio> queryEdificio = em.createQuery(
                    "SELECT e FROM Edificio e", Edificio.class);
            List<Edificio> edificios = queryEdificio.getResultList();

            for (Edificio e : edificios) {
                System.out.println("Dirección: " + e.getDireccion());
                System.out.println("Propietario: " + e.getPropietario().getNombre());
                System.out.println("Total departamentos: " + e.getTotalDepartamentos());
            }

        } finally {
            // 6. Cerrar la conexión
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
            emf.close();
        }
    }
}