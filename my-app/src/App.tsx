import './App.css';
import React, { useEffect, useState } from 'react';
import './index.css';
import { Table } from 'antd';
import moment from 'moment';
import type { ColumnsType, TableProps } from 'antd/es/table';
import { stringify } from 'querystring';

interface Transaction {
  id: React.Key;
  transDate: string;
  vendor: string;
  type: string;
  price: number;
  category: string;
}

type Category = {
  category: string;
  price: number;
  average: number;
};

const columns: ColumnsType<Transaction> = [
  {
    title: 'Transaction Date',
    dataIndex: 'transDate',
    sorter: (a, b) => moment(a.transDate).unix() - moment(b.transDate).unix(),
    // sortDirections: ['descend'],
    defaultSortOrder: 'descend',
    render: (value, row, index) => {
      let date = moment(new Date(value));
      return date.format("DD/MM/YYYY");
    }
  },
  {
    title: 'Vendor',
    dataIndex: 'vendor',
    // defaultSortOrder: 'descend',
    sorter: (a, b) => a.vendor.localeCompare(b.vendor),
  },
  {
    title: 'Type',
    dataIndex: 'type',
    sorter: (a, b) => a.type.localeCompare(b.type),
  },
  {
    title: 'Amount',
    dataIndex: 'price',
    sorter: (a, b) => a.price - b.price,
    render: (value, row, index) => {
      // do something like adding commas to the value or prefix
      return <span>£ {value.toLocaleString('en-US')}</span>;
    }
  },
  {
    title: 'Category',
    dataIndex: 'category',
    sorter: (a, b) => a.category.localeCompare(b.category),
  },
];

const onChange: TableProps<Transaction>['onChange'] = (pagination, filters, sorter, extra) => {
  console.log('params', pagination, filters, sorter, extra);
};

const App: React.FC = () => {
  const [transactions, setTransactions] = useState<[]>();
  const [totalAmountbyCat, setTotalAmountbyCat] = useState({});
  const [higestByCat, setHigestByCat] = useState({});
  const [lowestByCat, setLowestByCat] = useState({});
  const [average, setAverage] = useState<Category[]>();

  useEffect(() => {
    fetch("http://localhost:8080/transaction/2020", {
      method: 'GET',
      headers: {
        // 'Authorization': 'mysecrettoken',
        'Access-Control-Allow-Origin': '*'
      }
    })
      .then(async response => {
        const isJson = response.headers.get('content-type')?.includes('application/json');
        const result = isJson && await response.json();

        setTransactions(result);

        const highest = result?.reduce((prev: any, { category, price }: { category: string; price: number; }) => {
          prev[category] = prev[category] ? prev[category] < price ? price : prev[category] : price;
          return prev;
        }, {});

        setHigestByCat(highest);

        const lowest = result?.reduce((prev: any, { category, price }: { category: string; price: number; }) => {
          prev[category] = prev[category] ? prev[category] > price ? price : prev[category] : price;
          return prev;
        }, {});
        setLowestByCat(lowest);


        const totalAmount = result?.reduce((prev: any, { category, price }: { category: string; price: number; }) => {
          prev[category] = prev[category] ? prev[category] + price : price;
          return prev;
        }, {});

        setTotalAmountbyCat(totalAmount);

        let categoryAvg = {};
        let prev: any[] = [];

        result?.forEach(function (obj: Transaction) {
          console.info(obj.category)
          categoryAvg = {
            category: obj.category,
            price: obj.price,
            average: 1
          };
          prev.push(categoryAvg);
        });


        const groups = prev.reduce((acc, obj) => {
          const name = obj.category;
          if (acc[name]) {
            if (obj.price) (acc[name].price += obj.price) && ++acc[name].average;
          }
          else {
            acc[name] = obj;
            acc[name].average = 1;
            // taking 'Average' attribute as an items counter(on the first phase)
          };
          return acc;
        }, {});
        const averageResult = Object.keys(groups).map(name => {
          groups[name].average = groups[name].price / groups[name].average;
          return groups[name];
        });

        console.info(JSON.stringify(averageResult));
        setAverage(averageResult);

        if (!response.ok) {
          return Promise.reject(response.status);
        }
      })
      .catch(error => {
        console.error('There was an error!', error);
      })
  }, [])

  return (
    <div>
      <h3>All transactions for a given category:</h3> <br />
      <Table rowKey={record => record.id} columns={columns} dataSource={transactions} onChange={onChange} />

      <h3>Monthly average spend in a given category:</h3> <br />
      {average?.map(transaction => {
        return (
          <div>
            Category: {transaction.category}<br/>
            Average: {transaction.average}<br/><br/>
          </div>
        );
      })}

      <h3>Total outgoing per category</h3> <br />
      {(Object.keys(totalAmountbyCat) as (keyof typeof totalAmountbyCat)[]).map((key, i) => (
        <p key={i}>
          <span>Category: {key} <br /></span>
          <span>Total Amount: {totalAmountbyCat[key]}<br /></span>
        </p>
      ))}

      <h3>Highest spend in a given category</h3><br />
      {(Object.keys(higestByCat) as (keyof typeof higestByCat)[]).map((key, i) => (
        <p key={i}>
          <span>Category: {key} <br /></span>
          <span>Total Amount: {higestByCat[key]}<br /></span>
        </p>
      ))}

      <h3> Lowest spend in a given category</h3> <br />
      {(Object.keys(lowestByCat) as (keyof typeof lowestByCat)[]).map((key, i) => (
        <p key={i}>
          <span>Category: {key} <br /></span>
          <span>Total Amount: {lowestByCat[key]}<br /></span>
        </p>
      ))}
    </div>
  );


}


export default App;