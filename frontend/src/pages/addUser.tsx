import React, { useState } from 'react';
import { Container, Form, Button, Row, Col, Alert } from 'react-bootstrap';
import { createUser } from '../connection';

const AddUser = () => {
  const userLogged = JSON.parse(localStorage.getItem('user') as string);
  const [data, setData] = useState(null as any)
  const [error, setError] = useState('');

  const addUser = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const name = (e.currentTarget.elements[0] as HTMLInputElement).value;
    const email = (e.currentTarget.elements[1] as HTMLInputElement).value;
    const role = userLogged.role === 'SUPERADMIN' ? (e.currentTarget.elements[2] as HTMLSelectElement).value : 'USER';

    const data = await createUser(name, email, role);
    if (data === 400) setError('Użytkownik o podanym emailu już istnieje');
    else if (data) setData(data);
    else setError('Coś poszło nie tak');
  }

  return (
    <Container className="my-4">
      <Row className="justify-content-center">
        <Col md={6}>
          <h1 className="text-center mb-4">Tworzenie użytkownika</h1>
          {data && (
            <Alert variant="success">
              Użytkownik został utworzony pomyślnie!<br />
              Nazwa: {data.name}<br />
              Email: {data.email}<br />
              Rola: {data.role}<br />
              Hasło: {data.password}
            </Alert>
          )}
            {error && (
                <Alert variant="danger">
                {error}
                </Alert>
            )}
          <Form onSubmit={addUser}>
            <Form.Group controlId="formName">
              <Form.Label>Nazwa</Form.Label>
              <Form.Control type="text" name="name" required />
            </Form.Group>

            <Form.Group controlId="formEmail" className="mt-3">
              <Form.Label>Email</Form.Label>
              <Form.Control type="email" name="email" required />
            </Form.Group>

            {userLogged.role === 'SUPERADMIN' && (
              <Form.Group controlId="formRole" className="mt-3">
                <Form.Label>Rola</Form.Label>
                <Form.Control as="select" name="role">
                  <option value="USER">Użytkownik</option>
                  <option value="ADMIN">Administrator</option>
                </Form.Control>
              </Form.Group>
            )}

            <Button variant="primary" type="submit" className="mt-4 w-100">
              Dodaj
            </Button>
          </Form>
        </Col>
      </Row>
    </Container>
  );
};

export default AddUser;