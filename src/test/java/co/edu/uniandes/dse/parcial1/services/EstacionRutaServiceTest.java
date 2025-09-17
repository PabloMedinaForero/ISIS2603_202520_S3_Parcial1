package co.edu.uniandes.dse.parcial1.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.parcial1.entities.EstacionEntity;
import co.edu.uniandes.dse.parcial1.entities.RutaEntity;
import co.edu.uniandes.dse.parcial1.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(EstacionRutaService.class)
public class EstacionRutaServiceTest {

    @Autowired
    private EstacionRutaService estacionRutaService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory =  new PodamFactoryImpl();

    private RutaEntity rutaEntity = new RutaEntity();
    private List<EstacionEntity> estaciones = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from RutaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from EstacionEntity").executeUpdate();
    }

    private void insertData() {
		rutaEntity = factory.manufacturePojo(RutaEntity.class);
		entityManager.persist(rutaEntity);

		for (int i = 0; i < 3; i++) {
			EstacionEntity entity = factory.manufacturePojo(EstacionEntity.class);
			entityManager.persist(entity);
			entity.getRutas().add(rutaEntity);
			estaciones.add(entity);
			rutaEntity.getEstaciones().add(entity);	
		}
	}


    @Test
    void testAddRuta() throws EntityNotFoundException, IllegalOperationException{
        EstacionEntity estacionNueva = factory.manufacturePojo(EstacionEntity.class);
        entityManager.persist(estacionNueva);

        RutaEntity ruta = factory.manufacturePojo(RutaEntity.class);
        entityManager.persist(ruta);

        estacionRutaService.addEstacionRuta(estacionNueva.getId(), ruta.getId());

        RutaEntity lastRuta = estacionRutaService.getRutas(estacionNueva.getId(), ruta.getId()).getLast();
        assertEquals(lastRuta.getColor(), ruta.getColor());
        assertEquals(lastRuta.getEstaciones().size(), ruta.getEstaciones().size());
        assertEquals(lastRuta.getId(), ruta.getId());
        assertEquals(lastRuta.getNombre(), ruta.getNombre());
    }

    @Test
    void testAddInvalidRuta() throws EntityNotFoundException, IllegalOperationException {
        assertThrows(EntityNotFoundException.class, ()->{
			EstacionEntity nuevaEstacion = factory.manufacturePojo(EstacionEntity.class);
			entityManager.persist(nuevaEstacion);
			estacionRutaService.addEstacionRuta(nuevaEstacion.getId(), 0L);
		});
    }
    
}
