describe('Login', () => {
  it('abre /login, autentica credenciales validas y redirige a dashboard', () => {
    cy.fixture('auth').then(({ valid }) => {
      cy.loginUi(valid.correo, valid.password);
      cy.url().should('include', '/dashboard');
      cy.get('[data-testid="dashboard-page"]').should('be.visible');
    });
  });

  it('muestra manejo de error con credenciales invalidas', () => {
    cy.fixture('auth').then(({ invalid }) => {
      cy.loginUi(invalid.correo, invalid.password);
      cy.url().should('include', '/login');
      cy.get('[data-testid="login-error"]').should('be.visible');
    });
  });
});
