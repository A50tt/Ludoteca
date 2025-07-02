import { Loan } from "./Loan";
import { LoanPage } from "./LoanPage";

export const LOAN_DATA: LoanPage = {
    content: [
        {
            id: 1,
            game: {
                id: 1,
                title: "Game 1",
                age: 2011,
                category: {
                    id: 1,
                    name: "Category 1"
                },
                author: {
                    id: 1,
                    name: "Author 1",
                    nationality: "Country 1"
                },
            },
            client: {
                id: 1,
                name: "Client 1"
            },
            startDate: new Date('2025-01-01'),
            endDate: new Date('2025-01-10')
        },
        {
            id: 2,
            game: {
                id: 2,
                title: "Game 2",
                age: 2012,
                category: {
                    id: 2,
                    name: "Category 2"
                },
                author: {
                    id: 2,
                    name: "Author 2",
                    nationality: "Country 2"
                },
            },
            client: {
                id: 2,
                name: "Client 2"
            },
            startDate: new Date('2025-02-01'),
            endDate: new Date('2025-02-10')
        },
        {
            id: 3,
            game: {
                id: 3,
                title: "Game 3",
                age: 2013,
                category: {
                    id: 3,
                    name: "Category 3"
                },
                author: {
                    id: 3,
                    name: "Author 3",
                    nationality: "Country 3"
                },
            },
            client: {
                id: 3,
                name: "Client 3"
            },
            startDate: new Date('2025-03-01'),
            endDate: new Date('2025-03-10')
        },
        {
            id: 4,
            game: {
                id: 4,
                title: "Game 4",
                age: 2014,
                category: {
                    id: 4,
                    name: "Category 4"
                },
                author: {
                    id: 4,
                    name: "Author 4",
                    nationality: "Country 4"
                },
            },
            client: {
                id: 4,
                name: "Client 4"
            },
            startDate: new Date('2025-04-01'),
            endDate: new Date('2025-04-10')
        },
        {
            id: 5,
            game: {
                id: 5,
                title: "Game 5",
                age: 2015,
                category: {
                    id: 5,
                    name: "Category 5"
                },
                author: {
                    id: 5,
                    name: "Author 5",
                    nationality: "Country 5"
                },
            },
            client: {
                id: 5,
                name: "Client 5"
            },
            startDate: new Date('2025-05-01'),
            endDate: new Date('2025-05-10')
        }
    ],
    pageable: {
        pageSize: 3,
        pageNumber: 0,
        sort: [{ property: 'id', direction: 'ASC' }],
    },
    totalElements: 5,
}