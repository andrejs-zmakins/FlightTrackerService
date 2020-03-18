package flights.flighttracker.airport;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

/**
 * DAO service for Airport.
 * @author Andrejs Zmakins
 */
@Repository
@Transactional
public class AirportDaoService {

    @PersistenceContext
    private EntityManager entityManager;


    public Airport save(Airport airport) {
        this.entityManager.persist(airport);
        return airport;
    }


    public List<Airport> findAll() {
        // TODO: There are too many warnings here! Is it the right way?

        // TODO: Is it the right way to query all airports?
        Query query = entityManager.createQuery("SELECT a FROM Airport a");

        return (List<Airport>) query.getResultList();
    }


    public Airport findById(int id) {
        return entityManager.find(Airport.class, id);
    }
}
