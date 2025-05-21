import { useState } from "react";
import { Container, Form, Button, Row, Col, Alert } from 'react-bootstrap';
import { createTeam } from "../connection";
import { useNavigate } from 'react-router-dom';

const Addteams = () => {
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const addteams = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const name = (e.currentTarget.elements[0] as HTMLInputElement).value;
        const description = (e.currentTarget.elements[1] as HTMLInputElement).value;

        const data =await createTeam(name, description);

        if(data){
            navigate(`/teams/${data.id}`);
        }else{
            setError('Coś poszło nie tak');
        }
    }
    return (
        <Container className="my-4">
            <Row className="justify-content-center">
                <Col md={6}>
                <h1 className="text-center mb-4">Tworzenie zespołu</h1>
                {error && (
                    <Alert variant="danger">
                    {error}
                    </Alert>
                )}
                <Form onSubmit={addteams}>
                    <Form.Group controlId="formName">
                    <Form.Label>Nazwa</Form.Label>
                    <Form.Control type="text" name="name" required />
                    </Form.Group>

                    <Form.Group controlId="formDescription" className="mt-3">
                    <Form.Label>Opis</Form.Label>
                    <Form.Control type="text" name="description" />
                    </Form.Group>

                    <Button variant="primary" type="submit" className="mt-4 w-100">
                    Dodaj
                    </Button>
                </Form>
                </Col>
            </Row>
        </Container>
    );
}

export default Addteams;