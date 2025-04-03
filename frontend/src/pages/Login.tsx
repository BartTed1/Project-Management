import { Container, Row, Col, Form, Button, Alert } from 'react-bootstrap';
import { useState } from 'react';

import { login } from '../connection';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(''); 
       
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!email || !password) {
      setError('Email i hasło są wymagane');
      return;
    }
    setError('');
    
    if (await login(email, password, setError)) {
      location.reload();
    }
  };

  return (
    <Container className="d-flex align-items-center justify-content-center dark-background" style={{ minHeight: '100vh' }}>
      <Row className="w-100">
        <Col xs={12} md={6} lg={4} className="mx-auto">
          <h2 className="text-center mb-4">Logowanie</h2>
          {error && <Alert variant="danger">{error}</Alert>}
          <Form onSubmit={handleSubmit}>
            <Form.Group controlId="formEmail">
              <Form.Label>Email</Form.Label>
              <Form.Control 
                type="email" 
                placeholder="Wprowadź email" 
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </Form.Group>

            <Form.Group controlId="formPassword" className="mt-3">
              <Form.Label>Hasło</Form.Label>
              <Form.Control 
                type="password" 
                placeholder="Wprowadź hasło" 
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </Form.Group>

            <Button variant="primary" type="submit" className="w-100 mt-4">
              Zaloguj
            </Button>
            <Button variant="secondary" className="w-100 mt-4" href="/register">
              Zarejestruj się
            </Button>
          </Form>
        </Col>
      </Row>
    </Container>
  );
}

export default Login;