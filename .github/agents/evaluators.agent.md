# Crear Tests de Integración para Evaluadores

## Objetivo
Crear tests de integración efectivos para todos los evaluadores de notificaciones.
Cada test debe verificar que el evaluador se activa correctamente cuando existen los datos apropiados.

## Instrucciones para el Agente

### 1. Preparación
1. Crear una nueva rama: `feature/evaluator-integration-tests`
2. Analizar cada evaluador en `src/Service/Notification/Evaluator/`
3. Entender qué datos necesita cada uno para activarse

### 2. Estructura de Tests
Crear tests en `tests/Integration/Service/Notification/Evaluator/`

```php
<?php

declare(strict_types=1);

namespace App\Tests\Integration\Service\Notification\Evaluator;

use App\Entity\EoloFarm;
use App\Entity\NotificationEvent;
use App\Service\Notification\Evaluator\{EvaluatorName}Evaluator;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Test\KernelTestCase;

/**
 * Tests de integración para {EvaluatorName}Evaluator.
 *
 * Estos tests crean datos reales en la BD, ejecutan el evaluador,
 * y limpian los datos al finalizar.
 */
class {EvaluatorName}EvaluatorIntegrationTest extends KernelTestCase
{
    private EntityManagerInterface $entityManager;
    private {EvaluatorName}Evaluator $evaluator;
    private array $createdEntities = [];

    protected function setUp(): void
    {
        self::bootKernel();
        $container = static::getContainer();

        $this->entityManager = $container->get(EntityManagerInterface::class);
        $this->evaluator = $container->get({EvaluatorName}Evaluator::class);
    }

    protected function tearDown(): void
    {
        // Limpiar todas las entidades creadas en orden inverso
        foreach (array_reverse($this->createdEntities) as $entity) {
            $this->entityManager->remove($entity);
        }
        $this->entityManager->flush();

        parent::tearDown();
    }

    /**
     * Registra una entidad para limpieza automática.
     */
    private function trackEntity(object $entity): object
    {
        $this->createdEntities[] = $entity;
        return $entity;
    }
}
```

### 3. Patrón de Test con Datos

```php
public function testEvaluateReturnsTrueWhenConditionMet(): void
{
    // Arrange: Obtener un parque existente
    $farm = $this->entityManager->getRepository(EoloFarm::class)
        ->findOneBy([]);
    $this->assertNotNull($farm, 'Debe existir al menos un parque');

    // Arrange: Crear datos que activen el evaluador
    $testData = $this->createTestDataThatTriggersEvaluation($farm);
    $this->entityManager->flush();

    // Arrange: Crear evento de notificación
    $event = $this->createNotificationEvent('CODIGO-EVENTO');

    // Act
    $result = $this->evaluator->evaluate($event, $farm);

    // Assert
    $this->assertTrue($result, 'El evaluador debe activarse con estos datos');
}

public function testEvaluateReturnsFalseWhenConditionNotMet(): void
{
    // Arrange: Obtener un parque existente sin crear datos adicionales
    $farm = $this->entityManager->getRepository(EoloFarm::class)
        ->findOneBy([]);

    $event = $this->createNotificationEvent('CODIGO-EVENTO');

    // Act: Sin datos que activen, debe retornar false
    $result = $this->evaluator->evaluate($event, $farm);

    // Assert
    $this->assertFalse($result);
}

public function testGeneratePayloadReturnsValidStructure(): void
{
    // Arrange
    $farm = $this->getTestFarm();
    $this->createTestDataThatTriggersEvaluation($farm);
    $this->entityManager->flush();

    $event = $this->createNotificationEvent('CODIGO-EVENTO');

    // Act
    $payload = $this->evaluator->generatePayload($event, $farm);

    // Assert: Verificar estructura del payload
    $this->assertArrayHasKey('incident_period', $payload);
    $this->assertArrayHasKey('affected_objects', $payload);
    $this->assertArrayHasKey('params', $payload);
}
```

### 4. Helpers Comunes

```php
/**
 * Obtiene un parque existente para tests.
 */
private function getTestFarm(): EoloFarm
{
    $farm = $this->entityManager->getRepository(EoloFarm::class)
        ->createQueryBuilder('f')
        ->addSelect('a')
        ->leftJoin('f.eoloAeros', 'a')
        ->setMaxResults(1)
        ->getQuery()
        ->getOneOrNullResult();

    $this->assertNotNull($farm, 'Debe existir al menos un parque con aeros');
    return $farm;
}

/**
 * Obtiene un aero existente para tests.
 */
private function getTestAero(): EoloAero
{
    return $this->entityManager->getRepository(EoloAero::class)
        ->findOneBy([]);
}

/**
 * Crea un evento de notificación para testing.
 */
private function createNotificationEvent(string $code): NotificationEvent
{
    // Buscar evento existente o crear mock
    $event = $this->entityManager->getRepository(NotificationEvent::class)
        ->findOneBy(['code' => $code]);

    if (!$event) {
        $event = new NotificationEvent();
        $event->setCode($code);
        // No persistir, solo usar para el test
    }

    return $event;
}
```

### 5. Datos de Test por Evaluador

Para cada evaluador, crear los datos mínimos que lo activen:

#### LowAvailabilityEvaluator (I-04, I-05)
```php
private function createLowAvailabilityData(EoloFarm $farm): void
{
    $aero = $farm->getEoloAeros()->first();

    // Crear clarificaciones de baja disponibilidad para últimos 12 meses
    for ($i = 0; $i < 6; $i++) {
        $clarification = new EoloAeroClarificationLowAvailability();
        $clarification->setEoloAero($aero);
        $clarification->setMonth(new \DateTime("-{$i} months"));
        $clarification->setAvailability(0.5); // 50% disponibilidad

        $this->entityManager->persist($clarification);
        $this->trackEntity($clarification);
    }
}
```

#### MonthlyValuesEvaluator (I-01, I-02)
```php
private function createMissingMonthlyData(EoloFarm $farm): void
{
    // No crear datos = simular que faltan
    // El evaluador detecta ausencia de EoloMonthlyValues
}
```

#### GridLimitationsEvaluator (I-16)
```php
private function createGridLimitationData(EoloFarm $farm): void
{
    $limitation = new EoloFarmNetLimitation();
    $limitation->setEoloFarm($farm);
    $limitation->setLimitationDate(new \DateTime('-1 week'));
    $limitation->setEnergyLostMwh(500.0);
    $limitation->setType('grid');

    $this->entityManager->persist($limitation);
    $this->trackEntity($limitation);
}
```

### 6. Ejecución
```bash
# Ejecutar en el contenedor
docker compose exec frankenphp vendor/bin/phpunit tests/Integration/Service/Notification/Evaluator/ --testdox
```

### 7. Checklist por Evaluador

Para cada evaluador:
- [ ] Identificar eventos que implementa (I-XX, S-XX)
- [ ] Analizar método `evaluate()` para entender condiciones
- [ ] Identificar entidades/repositorios que consulta
- [ ] Crear datos mínimos que cumplan las condiciones
- [ ] Test positivo: datos que activan el evaluador
- [ ] Test negativo: sin datos o datos que no cumplen
- [ ] Test de payload: verificar estructura de respuesta
- [ ] Limpiar datos en tearDown

---

## Evaluadores a Testear

| Evaluador | Eventos | Datos Necesarios |
|-----------|---------|------------------|
| LowAvailabilityEvaluator | I-04, I-05 | EoloAeroClarificationLowAvailability |
| MonthlyValuesEvaluator | I-01, I-02 | Ausencia de EoloMonthlyValues |
| GridLimitationsEvaluator | I-16 | EoloFarmNetLimitation |
| StopControlEvaluator | I-22 | EoloStopControl |
| MajorCorrectivesEvaluator | I-11, S-02 | EoloLargeCorrective |
| PreventiveAdvancesEvaluator | ? | ? |
| FarmIntroductionEvaluator | I-06, I-07, I-08 | Depende del evento |
| DailyWorkForecastEvaluator | ? | EoloDailyForecast |
| WeeklyWorkForecastEvaluator | ? | ? |
| PendingAerosEvaluator | ? | Aeros pendientes |
| SpecificInfoEntryEvaluator | ? | ? |
| MeetingMinutesEvaluator | ? | ? |

---

## Variables
- $EVALUATOR_NAME: Nombre del evaluador (ej: LowAvailability)
- $EVENT_CODES: Códigos de eventos que implementa
- $REQUIRED_ENTITIES: Entidades que necesita para activarse
