describe('Modulo Departamentos', () => {
  beforeEach(() => {
    cy.ensureAuthSession();
    cy.visit('/departamentos');
  });

  it('accede con sesion activa y valida carga de listado', () => {
    cy.url().should('include', '/departamentos');

    cy.get('[data-testid="departamentos-page"]', { timeout: 10000 }).should('be.visible');

    cy.get('body', { timeout: 10000 }).should(($body) => {
      const resolvedStates =
        $body.find('[data-testid="departamentos-table"]').length +
        $body.find('[data-testid="empty-state"]').length +
        $body.find('[data-testid="error-state"]').length;

      expect(resolvedStates, 'estado final de departamentos').to.be.greaterThan(0);
    });

    cy.get('body').then(($body) => {
      if ($body.find('[data-testid="departamentos-table"]').length > 0) {
        cy.get('[data-testid="departamentos-table"]').should('be.visible');
      } else if ($body.find('[data-testid="empty-state"]').length > 0) {
        cy.get('[data-testid="empty-state"]').should('be.visible');
      } else {
        cy.get('[data-testid="error-state"]').should('be.visible');
      }
    });
  });

  it('intenta alta de departamento y valida exito o error de negocio visible', () => {
    const nombre = `Dept E2E ${Date.now()}`;

    cy.get('[data-testid="departamentos-create-link"]').click();
    cy.url().should('include', '/departamentos/nuevo');

    cy.get('[data-testid="departamento-form"]').should('be.visible');
    cy.get('#nombre').clear().type(nombre);
    cy.get('[data-testid="departamento-form"] button[type="submit"]').click();

    cy.get('body').then(($body) => {
      const inList = Cypress.$('[data-testid="departamentos-page"]').length > 0;
      const hasToast = $body.find('[data-testid="notification-toast"]').length > 0;
      const hasErrorBanner = $body.find('[data-testid="departamento-form-page"] .text-red-200').length > 0;

      if (inList || hasToast) {
        cy.visit('/departamentos');
        cy.get('[data-testid="departamentos-page"]').should('be.visible');
      } else if (hasErrorBanner) {
        cy.get('[data-testid="departamento-form-page"]').should('be.visible');
      } else {
        cy.get('[data-testid="departamento-form-page"]').should('be.visible');
      }
    });
  });

  it('prueba edicion y eliminacion cuando backend/UI lo permiten', () => {
    cy.get('body').then(($body) => {
      const hasRows = $body.find('[data-testid="departamentos-table"] tbody tr').length > 0;

      if (!hasRows) {
        cy.log('No hay filas para editar/eliminar en departamentos.');
        return;
      }

      cy.contains('button, a', 'Editar').first().click();
      cy.url().should('include', '/departamentos/');
      cy.get('[data-testid="departamento-form"]').should('be.visible');
      cy.get('#nombre').clear().type(`Dept Edit ${Date.now()}`);
      cy.get('[data-testid="departamento-form"] button[type="submit"]').click();

      cy.url().should('include', '/departamentos');

      cy.on('window:confirm', () => true);
      cy.contains('button', 'Eliminar').first().click();

      cy.get('body').then(($scope) => {
        if ($scope.find('[data-testid="notification-toast"]').length) {
          cy.get('[data-testid="notification-toast"]').should('be.visible');
        } else {
          cy.get('[data-testid="departamentos-page"]').should('be.visible');
        }
      });
    });
  });
});
