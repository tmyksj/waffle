package waffle.batch.step

import org.springframework.batch.core.Step

/**
 * Executes a given regression test.
 * The test will be persisted at each step.
 */
interface WebRegStep : Step
