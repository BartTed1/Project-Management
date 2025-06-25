import { useEffect, useState } from "react";
import { Alert, Button, Col, Container, Row, Form } from "react-bootstrap";
import { useParams } from "react-router-dom";
import { createTask, getTeam } from "../connection";

const AddTask = () => {
    const { id } = useParams<{ id: string }>();
    const [error, setError] = useState('');
    const [team, setTeam] = useState<any>();
    const [succes, setSucces] = useState(false);

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

    const addTask = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const title = (e.currentTarget.elements[0] as HTMLInputElement).value;
        const priority = (e.currentTarget.elements[1] as HTMLSelectElement).value;
        const deadline = (e.currentTarget.elements[2] as HTMLInputElement).value;
        const formattedDeadline = new Date(deadline).toISOString();
        const user = (e.currentTarget.elements[3] as HTMLSelectElement).value;
        const description = (e.currentTarget.elements[4] as HTMLInputElement).value;
        // alert(`${Number(id)}, ${title}, ${priority}, ${formattedDeadline}, ${Number(user)}, ${description}`);

        if(await createTask(Number(id), title, priority, formattedDeadline, Number(user), description)){
            setSucces(true);
            setError('');
        }else{
            setError('Coś poszło nie tak');
            setSucces(false);
        }
            
    }

    if (!team) return <></>;

    return (
        <Container className="my-4">
            <Row className="justify-content-center">
                <Col md={6}>
                <h1 className="text-center mb-4">Tworzenie zadania</h1>
                {succes && (
                    <Alert variant="success">
                        Zadanie zostało utworzone pomyślnie!
                    </Alert>
                )}
                {error && (
                    <Alert variant="danger">
                    {error}
                    </Alert>
                )}
                <Form onSubmit={addTask}>
                    <Form.Group controlId="formTitle">
                    <Form.Label>Tytuł</Form.Label>
                    <Form.Control type="text" name="title" required />
                    </Form.Group>

                    <Form.Group controlId="formPriority" className="mt-3">
                        <Form.Label>Priorytet</Form.Label>
                        <Form.Control as="select" name="priority">
                            <option value="LOW">Niski</option>
                            <option value="MEDIUM">Średni</option>
                            <option value="HIGH">Wysoki</option>
                        </Form.Control>
                    </Form.Group>

                    <Form.Group controlId="formDeadline" className="mt-3">
                        <Form.Label>Deadline</Form.Label>
                        <Form.Control type="datetime-local" name="deadline" required defaultValue={new Date(new Date().setDate(new Date().getDate() + 7)).toISOString().slice(0, 16)}/>
                    </Form.Group>

                    <Form.Group controlId="formUser" className="mt-3">
                        <Form.Label>Przypisane do</Form.Label>
                        <Form.Control as="select" name="user">
                            {team.users.map((user: any) => (
                                <option value={user.id} key={user.id}>{`${user.name} <${user.email}>`}</option>
                            ))}
                        </Form.Control>
                    </Form.Group>

                    <Form.Group controlId="formDescription" className="mt-3">
                        <Form.Label>Opis</Form.Label>
                        <Form.Control as="textarea" name="description" style={{ height: '300px', resize: 'none' }} />
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

export default AddTask;