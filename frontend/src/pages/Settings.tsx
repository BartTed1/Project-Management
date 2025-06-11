import React from 'react';
import { Container, Form, Button, Row, Col } from 'react-bootstrap';
import { changeName as changeNameConn, changePassword as changePasswordConn } from '../connection';

const Settings = () => {
  const user = JSON.parse(localStorage.getItem('user') as string);

  const changeName = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const name = (e.currentTarget.elements[0] as HTMLInputElement).value;
    if(await changeNameConn(name)){
        alert('Zmieniono nazwę konta');
        location.reload();
    }else{
        alert('Coś poszło nie tak');
    }
  }

  const changePassword = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const oldPassword = (e.currentTarget.elements[0] as HTMLInputElement).value;
    const newPassword = (e.currentTarget.elements[1] as HTMLInputElement).value;
    const newPassword2 = (e.currentTarget.elements[2] as HTMLInputElement).value;
    if(newPassword !== newPassword2){
        alert('Hasła nie są takie same');
        return;
    }
    if(await changePasswordConn(oldPassword, newPassword)){
        alert('Zmieniono hasło');
        location.reload();
    }else{
        alert('hasło niepoprawne');
    }
  }

  return (
    <Container>
      <h1 className="my-4">Ustawienia konta</h1>
      <Form onSubmit={changeName}>
        <h2 className="my-3">Nazwa konta</h2>
        <Form.Group as={Row} controlId="formName">
          <Form.Label column sm={2}>Nazwa:</Form.Label>
          <Col sm={10}>
            <Form.Control type="text" placeholder={user.name} required />
          </Col>
        </Form.Group>
        <Button variant="primary" type="submit" className="my-3">Zapisz</Button>
      </Form>
      <Form onSubmit={changePassword} >
        <h2 className="my-3">Hasło</h2>
        <Form.Group as={Row} controlId="formOldPassword">
          <Form.Label column sm={2} >Stare hasło:</Form.Label>
          <Col sm={10}>
            <Form.Control type="password" required />
          </Col>
        </Form.Group>
        <Form.Group as={Row} controlId="formNewPassword" className="my-3">
          <Form.Label column sm={2}>Nowe hasło:</Form.Label>
          <Col sm={10}>
            <Form.Control type="password" required />
          </Col>
        </Form.Group>
        <Form.Group as={Row} controlId="formNewPassword2">
          <Form.Label column sm={2}>Powtórz hasło:</Form.Label>
          <Col sm={10}>
            <Form.Control type="password" required />
          </Col>
        </Form.Group>
        <Button variant="primary" type="submit" className="my-3">Zapisz</Button>
      </Form>
    </Container>
  );
}

export default Settings;