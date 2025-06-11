import { useEffect, useState } from "react";
import { Container, Form, Button, Row, Col, Alert } from 'react-bootstrap';
import { useParams } from "react-router-dom";
import { updateUser, getUser } from "../connection";

const EditUser = () => {
    const userLogged = JSON.parse(localStorage.getItem('user') as string);
    const { id } = useParams<{ id: string }>();
    const [data, setData] = useState(null as any)
    const [user, setUser] = useState(null as any)
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchUser = async () => {
          if (id) {
            const user = await getUser(id);
            if (user.error) setError(user.error);
            else setUser(user);
          }
        };
        fetchUser();
    }, [id]);

    if (!user) return <></>;

    const editUser = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const id = user.id;
        const name = (e.currentTarget.elements[0] as HTMLInputElement).value || user.name;
        const role = userLogged.role === 'SUPERADMIN' ? (e.currentTarget.elements[1] as HTMLSelectElement).value : user.role || user.role;

        const data = await updateUser(id, name, role);

        if (data) setData(data);
        else setError('Coś poszło nie tak');
    }

    return (
        <Container className="my-4">
        <Row className="justify-content-center">
            <Col md={6}>
            <h1 className="text-center mb-4">modyfikacja użytkownika</h1>
            {data && (
                <Alert variant="success">
                    Użytkownik został zmodyfikowany pomyślnie!<br />
                    Nazwa: {data.name}<br />
                    Email: {data.email}<br />
                    Rola: {data.role}<br />
                </Alert>
            )}
            {error && (
                <Alert variant="danger">
                {error}
                </Alert>
            )}
            <Form onSubmit={editUser}>
                <Form.Group controlId="formName">
                <Form.Label>Nazwa</Form.Label>
                <Form.Control type="text" name="name" required defaultValue={user.name} />
                </Form.Group>

                {userLogged.role === 'SUPERADMIN' && (
                    <Form.Group controlId="formRole" className="mt-3">
                        <Form.Label>Rola</Form.Label>
                        <Form.Control as="select" name="role" defaultValue={user.role}>
                            <option value="USER">Użytkownik</option>
                            <option value="ADMIN">Administrator</option>
                        </Form.Control>
                    </Form.Group>
                )}

                <Button variant="primary" type="submit" className="mt-4 w-100">
                Zmień
                </Button>
            </Form>
            </Col>
        </Row>
        </Container>
    );  
}

export default EditUser;