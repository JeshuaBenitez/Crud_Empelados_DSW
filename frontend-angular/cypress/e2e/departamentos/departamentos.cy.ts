describe('Departamentos module', () => {
  beforeEach(() => {
    cy.visit('/login');
    cy.get('[data-testid="login-email"]').clear().type('empleado.demo@empresa.com');
    cy.get('[data-testid="login-password"]').clear().type('Empleado123!');
    cy.get('[data-testid="login-submit"]').click();
    cy.url({ timeout: 10000 }).should('include', '/dashboard');
  });

  it('loads departamentos page', () => {
    cy.visit('/departamentos');
    cy.get('[data-testid="departamentos-page"]', { timeout: 10000 }).should('be.visible');
  });
});
