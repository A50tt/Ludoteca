import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: '',
        redirectTo: '/games', pathMatch: 'full'
    },
    {
        path: 'categories',
        loadComponent: () => import('../category/components/category-list/category-list.component').then(m => m.CategoryListComponent)
    },
    {
        path: 'authors',
        loadComponent: () => import('../author/components/author-list/author-list.component').then(m => m.AuthorListComponent)
    },
    {
        path: 'games',
        loadComponent: () => import('../game/components/game-list/game-list.component').then(m => m.GameListComponent)

    },
    {
        path: 'clients',
        loadComponent: () => import('../client/components/client-list/client-list.component').then(m => m.ClientListComponent)

    },
    {
        path: 'loans',
        loadComponent: () => import('../loan/components/loan-list/loan-list.component').then(m => m.LoanListComponent)

    }
];
