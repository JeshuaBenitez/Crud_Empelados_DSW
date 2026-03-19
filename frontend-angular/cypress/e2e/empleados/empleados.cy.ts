describe('Empleados module', () => {
  beforeEach(() => {
    cy.visit('/login');
    cy.get('[data-testid="login-email"]').type('empleado.demo@empresa.com');
    cy.get('[data-testid="login-password"]').type('Empleado123!');
    cy.get('[data-testid="login-submit"]').click();
  });

  it('loads empleados page', () => {
    cy.visit('/empleados');
    cy.get('[data-testid="empleados-page"]').should('be.visible');
  });
});
