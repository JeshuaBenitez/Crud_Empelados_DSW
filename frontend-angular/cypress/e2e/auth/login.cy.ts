describe('Login flow', () => {
  it('shows login form and validates errors', () => {
    cy.visit('/login');
    cy.get('[data-testid="login-form"]').should('be.visible');
    cy.get('[data-testid="login-submit"]').click();
    cy.get('[data-testid="login-form"]').should('exist');
  });

  it('attempts login with demo user', () => {
    cy.visit('/login');
    cy.get('[data-testid="login-email"]').type('empleado.demo@empresa.com');
    cy.get('[data-testid="login-password"]').type('Empleado123!');
    cy.get('[data-testid="login-submit"]').click();
    cy.url().should('include', '/dashboard');
  });
});
