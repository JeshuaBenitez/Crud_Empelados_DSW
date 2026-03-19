describe('Departamentos module', () => {
  beforeEach(() => {
    cy.visit('/login');
    cy.get('[data-testid="login-email"]').type('empleado.demo@empresa.com');
    cy.get('[data-testid="login-password"]').type('Empleado123!');
    cy.get('[data-testid="login-submit"]').click();
  });

  it('loads departamentos page', () => {
    cy.visit('/departamentos');
    cy.get('[data-testid="departamentos-page"]').should('be.visible');
  });
});
