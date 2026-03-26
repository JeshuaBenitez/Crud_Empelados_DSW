Cypress.Commands.add('loginUi', (correo, password) => {
	cy.visit('/login');
	cy.get('[data-testid="login-email"]').clear().type(correo);
	cy.get('[data-testid="login-password"]').clear().type(password, { log: false });
	cy.get('[data-testid="login-submit"]').click();
});

Cypress.Commands.add('loginApi', (correo, password) => {
	cy.request({
		method: 'POST',
		url: 'http://localhost:8081/api/v1/auth/empleados/login',
		body: { correo, password },
	}).then((response) => {
		expect(response.status).to.eq(200);
		expect(response.body.accessToken).to.be.a('string').and.not.be.empty;

		cy.window().then((win) => {
			win.sessionStorage.setItem('frontend.jwt.token', response.body.accessToken);
		});
	});
});

Cypress.Commands.add('ensureAuthSession', () => {
	cy.fixture('auth').then(({ valid }) => {
		cy.loginApi(valid.correo, valid.password);
	});
});