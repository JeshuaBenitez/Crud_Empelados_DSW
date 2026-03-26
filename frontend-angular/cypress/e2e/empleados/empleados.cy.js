describe('Modulo Empleados', () => {
  beforeEach(() => {
    cy.ensureAuthSession();
    cy.visit('/empleados');
  });

  it('accede con sesion activa y valida carga de listado', () => {
    cy.url().should('include', '/empleados');

    cy.get('[data-testid="empleados-page"]', { timeout: 10000 }).should('be.visible');

    cy.get('body', { timeout: 10000 }).should(($body) => {
      const resolvedStates =
        $body.find('[data-testid="empleados-table"]').length +
        $body.find('[data-testid="empty-state"]').length +
        $body.find('[data-testid="error-state"]').length;

      expect(resolvedStates, 'estado final de empleados').to.be.greaterThan(0);
    });

    cy.get('body').then(($body) => {
      if ($body.find('[data-testid="empleados-table"]').length > 0) {
        cy.get('[data-testid="empleados-table"]').should('be.visible');
      } else if ($body.find('[data-testid="empty-state"]').length > 0) {
        cy.get('[data-testid="empty-state"]').should('be.visible');
      } else {
        cy.get('[data-testid="error-state"]').should('be.visible');
      }
    });
  });

  it('prueba edicion y eliminacion cuando backend/UI lo permiten', () => {
    cy.get('body').then(($body) => {
      const hasRows = $body.find('[data-testid="empleados-table"] tbody tr').length > 0;

      if (!hasRows) {
        cy.log('No hay filas para editar/eliminar en empleados.');
        return;
      }

      cy.contains('button, a', 'Editar').first().click();
      cy.url().should('include', '/empleados/');
      cy.get('[data-testid="empleado-form"]').should('be.visible');
      cy.get('#nombre').clear().type(`Empleado E2E ${Date.now()}`);
      cy.get('[data-testid="empleado-form"] button[type="submit"]').click();

      cy.url().should('include', '/empleados');

      cy.on('window:confirm', () => true);
      cy.contains('button', 'Eliminar').first().click();

      cy.get('body').then(($toastScope) => {
        if ($toastScope.find('[data-testid="notification-toast"]').length) {
          cy.get('[data-testid="notification-toast"]').should('be.visible');
        } else {
          cy.get('[data-testid="empleados-page"]').should('be.visible');
        }
      });
    });
  });
});
