import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getTeam, updateTeam } from "../connection";
import { Alert, Container, Row, Form, Button, Col } from "react-bootstrap";

const EditTeams = () => {
    // const userLogged = JSON.parse(localStorage.getItem('user') as string);
    const { id } = useParams<{ id: string }>();
    const [team, setTeam] = useState(null as any)
    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchTeam = async () => {
            if (id) {
            const team = await getTeam(id);
            if (team.error) setError(team.error);
            else setTeam(team);
            }
        };
        fetchTeam();
    }, [id]);

    const editTeam = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const id = team.id;
        const name = (e.currentTarget.elements[0] as HTMLInputElement).value || team.name;
        const description = (e.currentTarget.elements[1] as HTMLInputElement).value || team.description;

        const data = await updateTeam(id, name, description);

        if (data) navigate(`/teams/${data.id}`);
        else setError('Coś poszło nie tak');
    }

    if (!team) return <></>;

    return (
        <Container className="my-4">
        <Row className="justify-content-center">
            <Col md={6}>
            <h1 className="text-center mb-4">modyfikacja zespołu</h1>

            {error && (
                <Alert variant="danger">
                {error}
                </Alert>
            )}
            <Form onSubmit={editTeam}>
                <Form.Group controlId="formName">
                <Form.Label>Nazwa</Form.Label>
                <Form.Control type="text" name="name" required defaultValue={team.name} />
                </Form.Group>

                <Form.Group controlId="formDescription" className="mt-3">
                <Form.Label>Opis</Form.Label>
                <Form.Control type="text" name="description"  defaultValue={team.description} />
                </Form.Group>


                <Button variant="primary" type="submit" className="mt-4 w-100">
                Zmień
                </Button>
            </Form>
            </Col>
        </Row>
        </Container>
    )
}

export default EditTeams;