import { useEffect, useState } from "react";
import { deleteTeam, getUser } from "../connection";
import { Table, Container, Alert } from "react-bootstrap";
import '../style/table.css';
import infoIcon from '../assets/info.svg';
import deleteIcon from '../assets/delete.svg';
import editIcon from '../assets/edit.svg';

const OwnTeams = () => {
    const [alertMsg, setAlertMsg] = useState(null as any);
    const [data, setData] = useState(null as any);
    const userLogged = JSON.parse(localStorage.getItem('user') as string)
    
    useEffect(() => {
        const fetchUsers = async () => {
            const data = await getUser(userLogged.id);
            setData(data);  
        };
        fetchUsers();
    }, []);

    const handleDelete = async (id: number) => {
        if(window.confirm('Czy na pewno chcesz usunąć zespół?')){
            if(await deleteTeam(id.toString())){
                setAlertMsg({
                    variant: 'success',
                    msg: 'pomyślnie usunięto zespół'
                });
                const newData = {
                    ...data,
                    teams: data.teams.filter((team: any) => team.team.id != id)
                };
                setData(newData);
            }else{
                setAlertMsg({
                    variant: 'danger',
                    msg: 'Coś poszło nie tak przy próbie usunięcia zespołu'
                });
            }
        }
    }
    

    if(!data)return <></>;
    return (
        <>
            <Container>
                <h1 className='my-4'>Twoje zespoły</h1>
                <a href='/addteams' className='btn btn-primary mb-4'>Utwórz zaspół</a>
                {alertMsg && <Alert variant={alertMsg.variant}>{alertMsg.msg}</Alert>}
                <div className="table-responsive">
                    <Table striped bordered hover variant='dark'>
                        <thead className='thead text-center'>
                            <tr>
                                <th>#</th>
                                <th>Nazwa</th>
                                <th>opis</th>
                                <th>nazwa właściciela</th>
                                <th>email właściciela</th>
                                <th>Akcje</th>
                            </tr>
                        </thead>
                        <tbody>
                            {data.teams.map((team: any) => (
                                <tr key={team.team.id}>
                                    <td>{team.team.id}</td>
                                    <td>{team.team.name}</td>
                                    <td>{team.team.description}</td>
                                    <td>{team.team.owner.name}</td>
                                    <td>{team.team.owner.email}</td>
                                    <td>
                                        <a href={`/teams/${team.team.id}`}><img src={infoIcon} alt='info' width='20px' title='szczegóły' /></a>
                                        {team.team.owner.id == userLogged.id &&
                                            <>
                                                <img src={deleteIcon} alt='del' width='20px' title='usuń' className='icon' onClick={() => handleDelete(team.team.id)} />
                                                <a href={`/teams/${team.team.id}/edit`}><img src={editIcon} alt='edit' width='20px' title='edytuj' /></a>
                                            </>
                                        }
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </Table>
                </div>
            </Container>
        </>
    );
}

export default OwnTeams;