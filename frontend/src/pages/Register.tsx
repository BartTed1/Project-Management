import { Container, Row, Col, Form, Button, Alert } from 'react-bootstrap';
import { useState } from 'react';

import { register } from '../connection';

const Register = () => {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(''); 
    const [succes, setSucces] = useState(false);
       
    const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setSucces(false);
    if (!email || !password || !firstName || !lastName) {
        setError('wszystkie dane są wymagane');
        return;
    }
    

    if (await register(email, password, firstName, lastName, email, setError)) {
        setSucces(true);
    }
    };

  return (
    <Container className="d-flex align-items-center justify-content-center dark-background" style={{ minHeight: '100vh' }}>
      <Row className="w-100">
        <Col xs={12} md={6} lg={4} className="mx-auto">
          <h2 className="text-center mb-4">Rejestracja</h2>
          {error && <Alert variant="danger">{error}</Alert>}
          {succes && <Alert variant="success">Zarejestrowano pomyślnie</Alert>}
          <Form onSubmit={handleSubmit}>
            <Form.Group controlId="formFirstName">
              <Form.Label>Imię</Form.Label>
              <Form.Control 
                type="text"
                placeholder="Wprowadź imię" 
                value={firstName}
                onChange={(e) => setFirstName(e.target.value)}
                required
              />
            </Form.Group>

            <Form.Group controlId="formLastName" className="mt-3">
              <Form.Label>Nazwisko</Form.Label>
              <Form.Control 
                type="text"
                placeholder="Wprowadź nazwisko" 
                value={lastName}
                onChange={(e) => setLastName(e.target.value)}
                required
              />
            </Form.Group>

            <Form.Group controlId="formEmail" className="mt-3">
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
                Zarejestruj się
            </Button>
            <Button variant="secondary" className="w-100 mt-4" href="/login">
                Zaloguj się
            </Button>
          </Form>
        </Col>
      </Row>
    </Container>
  );
}

export default Register;