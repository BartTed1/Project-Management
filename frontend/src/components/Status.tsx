import React from 'react';

interface StatusProps {
  status: 'DURING' | 'COMPLETED' | 'UNCOMPLETED';
}

const Status: React.FC<StatusProps> = ({ status }) => {
  let backgroundColor: string;
  let label: string;

  switch (status) {
    case 'DURING':
      backgroundColor = 'blue';
      label = 'w trakcie';
      break;
    case 'COMPLETED':
      backgroundColor = 'green';
      label = 'zakończony';
      break;
    case 'UNCOMPLETED':
      backgroundColor = 'red';
      label = 'nie zakończony';
      break;
    default:
      backgroundColor = 'gray';
      label = 'nieznany';
  }

  const style: React.CSSProperties = {
    backgroundColor,
    color: 'white',
    padding: '4px 8px',
    borderRadius: '4px',
    display: 'inline-block',
    textAlign: 'center' as 'center',
    fontWeight: 'bold',
    fontSize: '12px'
  };

  return (
    <div style={style}>
      {label}
    </div>
  );
};

export default Status;