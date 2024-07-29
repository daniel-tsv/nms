import { useState } from "react";
import { Auth } from "./features/auth/components/Auth/Auth";
import { getToken } from "./features/auth/util/auth";
import { Dashboard } from "./pages/Dashboard/Dashboard";

export const App = () => {
  const [authenticated, setAuthenticated] = useState(Boolean(getToken()));

  if (!authenticated) {
    return (
      <Auth setAuthenticated={(value: boolean) => setAuthenticated(value)} />
    );
  }
  return <Dashboard />;
};
