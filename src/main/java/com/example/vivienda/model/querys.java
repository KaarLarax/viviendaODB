package com.example.vivienda.model;

import javax.persistence.*;
import java.util.List;

public class querys {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vivienda-db");
        EntityManager em = emf.createEntityManager();

        try {
            // --- CONSULTAS OQL ---

            // 1. Número de viviendas en la colonia "Centro"
            String coloniaNombre = "Centro";
            Query q1 = em.createQuery(
                    "SELECT COUNT(v) FROM Vivienda v WHERE v.colonia.nombre = :nombreColonia");
            q1.setParameter("nombreColonia", coloniaNombre);
            Long numViviendas = (Long) q1.getSingleResult();
            System.out.println("1. Número de viviendas en la colonia " + coloniaNombre + ": " + numViviendas);

            // 2. Viviendas con más de 100 m² y su propietario
            Query q2 = em.createQuery(
                    "SELECT v.id, v.superficie, v.propietario.nombre FROM Vivienda v WHERE v.superficie > 100");
            List<Object[]> resultados2 = q2.getResultList();
            System.out.println("\n2. Viviendas con más de 100m²:");
            for (Object[] row : resultados2) {
                System.out.println("ID: " + row[0] + ", Superficie: " + row[1] + ", Propietario: " + row[2]);
            }

            // 3. Edificios con más de 10 departamentos de más de 100 m²
            Query q3 = em.createQuery(
                    "SELECT e FROM Edificio e JOIN e.departamentos d " +
                            "WHERE d.superficie > 100 " +
                            "GROUP BY e " +
                            "HAVING COUNT(d) > 10"
            );
            List<Edificio> resultados3 = q3.getResultList();
            System.out.println("\n3. Edificios con más de 10 departamentos >100m²:");
            for (Edificio e : resultados3) {
                System.out.println("Dirección: " + e.getDireccion() + ", Total Departamentos: " + e.getTotalDepartamentos());
            }


            // 4. Colonia con menor superficie construida
            Query q4 = em.createQuery(
                    "SELECT c FROM Colonia c JOIN c.viviendas v " +
                            "GROUP BY c " +
                            "ORDER BY SUM(v.superficie) ASC"
            );
            q4.setMaxResults(1); // Solo la colonia con menor superficie
            Colonia menorSuperficie = (Colonia) q4.getSingleResult();
            System.out.println("\n4. Colonia con menor superficie construida: " + menorSuperficie.getNombre());


            // 5. Integrantes de la familia "Pérez"
            String familiaX = "Pérez";
            Query q5 = em.createQuery(
                    "SELECT p.apellidos, p.nombre, p.edad FROM Persona p WHERE p.apellidos = :apellidoFamilia");
            q5.setParameter("apellidoFamilia", familiaX);

            List<Object[]> resultados5 = q5.getResultList(); // usar getResultList() para múltiples filas
            System.out.println("\n5. Integrantes de la familia " + familiaX + ":");
            if(resultados5.isEmpty()) {
                System.out.println("No se encontraron integrantes de la familia " + familiaX);
            } else {
                for (Object[] row : resultados5) {
                    System.out.println("Apellidos: " + row[0] + ", Nombre: " + row[1] + ", Edad: " + row[2]);
                }
            }


            // 6. Familia con mayor número de adultos mayores (>65)
            Query q6 = em.createQuery(
                    "SELECT p.nombre, COUNT(p) FROM Persona p WHERE p.edad > 65 GROUP BY p.nombre ORDER BY COUNT(p) DESC");
            List<Object[]> resultados6 = q6.getResultList();
            if(resultados6.isEmpty()) {
                System.out.println("\n6. No se encontraron familias con adultos mayores.");
            } else {
                Object[] familiaMayorAdultos = resultados6.get(0); // la primera familia con más adultos mayores
                System.out.println("\n6. Familia con mayor número de adultos mayores (>65): " +
                        familiaMayorAdultos[0] + ", Adultos mayores: " + familiaMayorAdultos[1]);
            }

            // 7. Personas con más de 3 propiedades (nombre y cantidad)
            Query q7 = em.createQuery(
                    "SELECT p.nombre, COUNT(v) " +
                            "FROM Vivienda v JOIN v.propietario p " +
                            "GROUP BY p " +
                            "HAVING COUNT(v) > 3"
            );

            List<Object[]> resultados7 = q7.getResultList();
            System.out.println("\n7. Personas con más de 3 propiedades:");
            if(resultados7.isEmpty()) {
                System.out.println("No se encontraron personas con más de 3 propiedades.");
            } else {
                for (Object[] row : resultados7) {
                    System.out.println("Nombre: " + row[0] + ", Propiedades: " + row[1]);
                }
            }

            // 8. Colonia con mayor número de viviendas en edificios
            Query q8 = em.createQuery(
                    "SELECT c.nombre, COUNT(d) " +
                            "FROM Edificio e JOIN e.departamentos d " +
                            "JOIN d.colonia c " +
                            "GROUP BY c.nombre " +
                            "ORDER BY COUNT(d) DESC"
            );
            q8.setMaxResults(1); // Solo la colonia con más viviendas en edificios
            List<Object[]> resultados8 = q8.getResultList();

            if(resultados8.isEmpty()) {
                System.out.println("\n8. No se encontraron viviendas en edificios.");
            } else {
                Object[] coloniaMayorEdificios = resultados8.get(0);
                System.out.println("\n8. Colonia con mayor número de viviendas en edificios: " +
                        coloniaMayorEdificios[0] + ", Total viviendas en edificios: " + coloniaMayorEdificios[1]);
            }

            // 9. Colonia con mayor número de viviendas <60m²
            Query q9 = em.createQuery(
                    "SELECT c.nombre, COUNT(v) " +
                            "FROM Vivienda v JOIN v.colonia c " +
                            "WHERE v.superficie < 60 " +
                            "GROUP BY c.nombre " +
                            "ORDER BY COUNT(v) DESC"
            );
            q9.setMaxResults(1); // Solo la primera
            List<Object[]> resultados9 = q9.getResultList();

            if(resultados9.isEmpty()) {
                System.out.println("\n9. No se encontraron viviendas <60m².");
            } else {
                Object[] coloniaViviendasPequenas = resultados9.get(0);
                System.out.println("\n9. Colonia con mayor número de viviendas <60m²: " +
                        coloniaViviendasPequenas[0] + ", Total: " + coloniaViviendasPequenas[1]);
            }

            // 10. Viviendas con habitantes (ID vivienda y nombre persona)
            Query q10 = em.createQuery(
                    "SELECT v.id, p.nombre " +
                            "FROM Vivienda v JOIN v.habitantes p"
            );
            List<Object[]> resultados10 = q10.getResultList();

            System.out.println("\n10. Viviendas con habitantes:");
            if(resultados10.isEmpty()) {
                System.out.println("No se encontraron viviendas con habitantes.");
            } else {
                for (Object[] row : resultados10) {
                    System.out.println("ID Vivienda: " + row[0] + ", Habitante: " + row[1]);
                }
            }


        } finally {
            em.close();
            emf.close();
        }
    }
}
