// Apollo client for connecting to GraphQL endpoints.
/*
import { ApolloClient, InMemoryCache } from '@apollo/client';

const client = new ApolloClient({
    uri: 'http://localhost:8080/api/graphql',
    cache: new InMemoryCache(),
});

export default client;
*/
// frontend/apollo-client.ts
import { ApolloClient, InMemoryCache, HttpLink } from '@apollo/client';

// Change this to your actual GraphQL endpoint
const httpLink = new HttpLink({
  uri:
    process.env.NEXT_PUBLIC_GRAPHQL_ENDPOINT ||
    'http://localhost:8080/api/graphql', // or your api-gateway/graphql URL
});

const client = new ApolloClient({
  link: httpLink,
  cache: new InMemoryCache(),
});

export default client;


