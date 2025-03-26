import React from 'react';
import { format } from 'date-fns';
import { pl } from 'date-fns/locale';

interface DateTimeProps {
  date: string;
}

const DateTime: React.FC<DateTimeProps> = ({ date }) => {
  const formattedDate = format(new Date(date), 'dd MMMM yyyy, HH:mm', { locale: pl });

  return (
    <>
      {formattedDate}
    </>
  );
};

export default DateTime;