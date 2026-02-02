Feature: Trainer workload integration

  Scenario: Create trainings for two trainees, delete one, verify trainer workload
    When I add the following trainings:
      | trainer     | trainee      | type       | name       | date       | hours |
      | John.Doe    | Sarah.Lee    | ZUMBA      | Zumba      | 2026-07-01 | 2     |
      | John.Doe    | Sarah.Lee    | ZUMBA      | Zumba      | 2026-07-02 | 3     |
      | John.Doe    | Mike.Wilson  | RESISTANCE | Resistance | 2026-07-03 | 4     |
    When I delete the trainee "Sarah.Lee"
    Then the total workload hours for trainer "John.Doe" for month 7 year 2026 should be 4

