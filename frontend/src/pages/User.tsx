import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getUser } from '../connection';
import { Container, Card, Row, Col, Table } from 'react-bootstrap';

const User = () => {
  const { id } = useParams<{ id: string }>();
  const [data, setData] = useState(null as any);
  const [error, setError] = useState(null as any);

  useEffect(() => {
    const fetchUser = async () => {
      if (id) {
        const data = await getUser(id);
        if (data.error) setError(data.error);
        else setData(data);
        console.log(data);
      }
    };
    fetchUser();
  }, [id]);

  if (error) return <div>{error}</div>;

  if (!data) {
        return <h1>Ładowanie</h1>;
    }


  return (
    <Container className="my-4">
      <Row className="justify-content-center">
        <Col md={8}>
          <Card bg="dark" text="white">
            <Card.Header as="h1">Szczegóły Użytkownika</Card.Header>
            <Card.Body>
              <Card.Text>
                <strong>Nazwa:</strong> {data.name}
              </Card.Text>
              <Card.Text>
                <strong>Email:</strong> {data.email}
              </Card.Text>
              <Card.Text>
                <strong>Rola:</strong> {data.role}
              </Card.Text>
            </Card.Body>
          </Card>
        </Col>
        {data.teams.length > 0 && (
          <Col md={8}>
            <Card bg="dark" text="white" className="mt-4">
              <Card.Header as="h1">Zespoły</Card.Header>
              <Card.Body>
                <div className="table-responsive">
                  <Table striped bordered hover variant='dark'>
                  <thead className='thead text-center'>
                    <tr>
                    <th>#</th>
                    <th>Nazwa</th>
                    <th>opis</th>
                    <th>nazwa właściciela</th>
                    <th>email właściciela</th>
                    </tr>
                  </thead>
                  <tbody>
                    {data.teams.map((team: any) => (
                    <tr key={team.id}>
                      <td>{team.id}</td>
                      <td>{team.name}</td>
                      <td>{team.description}</td>
                      <td>{team.owner.name}</td>
                      <td>{team.owner.email}</td>
                    </tr>
                    ))}
                  </tbody>
                  </Table>
                </div>
              </Card.Body>
            </Card>
          </Col>
        )}
      </Row>
    </Container>
  );
};

export default User;