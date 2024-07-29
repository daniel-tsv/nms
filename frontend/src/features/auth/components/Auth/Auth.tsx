import { FormEvent, useState } from "react";
import { Block } from "../../../../shared/components/Block/Block";
import { Button } from "../../../../shared/components/Button/Button";
import { Input } from "../../../../shared/components/Input/Input";
import { Layout } from "../../../../shared/components/Layout/Layout";
import { ErrorResponseDTO } from "../../../../shared/types";
import { handleError } from "../../../../shared/util/api";
import { useAppDispatch } from "../../../../store/hooks";
import { setUser } from "../../../user/slices/userSlice";
import { AuthService } from "../../services/AuthService";
import { AuthResponseDTO, LoginDTO, RegisterDTO } from "../../types";
import { setToken } from "../../util/auth";
import styles from "./Auth.module.css";

const Username = ({
  username,
  setUsername,
}: {
  username: string;
  setUsername: (newUsername: string) => void;
}) => (
  <>
    <p className={styles["user-field__label"]}>Username</p>
    <Input
      className={styles["user-field__input"]}
      value={username}
      onChange={(e) => setUsername(e.target.value)}
      autoFocus={true}
      type={"username"}
      placeholder={"Enter username"}
    />
  </>
);

const Password = ({
  password,
  setPassword,
}: {
  password: string;
  setPassword: (newPassword: string) => void;
}) => (
  <>
    <p className={styles["user-field__label"]}>Password</p>
    <Input
      className={styles["user-field__input"]}
      value={password}
      onChange={(e) => setPassword(e.target.value)}
      autoFocus={false}
      type={"password"}
      placeholder={"Enter password"}
    />
  </>
);

const Email = ({
  email,
  setEmail,
}: {
  email: string;
  setEmail: (newEmail: string) => void;
}) => (
  <>
    <p className={styles["user-field__label"]}>Email</p>
    <Input
      className={styles["user-field__input"]}
      value={email}
      onChange={(e) => setEmail(e.target.value)}
      autoFocus={false}
      type={"email"}
      placeholder={"Enter email"}
    />
  </>
);

export const Auth = ({
  setAuthenticated,
}: {
  setAuthenticated: (value: boolean) => void;
}) => {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [hasAccount, setHasAccount] = useState(true);
  const [errMsg, setErrMsg] = useState("");
  const dispatch = useAppDispatch();

  const handleAuth = async (e: FormEvent) => {
    e.preventDefault();
    setErrMsg("");

    try {
      const response: AuthResponseDTO | ErrorResponseDTO = hasAccount
        ? await AuthService.login({
            username,
            password,
          } as LoginDTO)
        : await AuthService.register({
            username,
            email,
            password,
          } as RegisterDTO);

      if ("error" in response) {
        setErrMsg(response.message);
        return;
      }

      dispatch(setUser(response.userDTO));
      setToken(response.jwtToken);
      setAuthenticated(true);
    } catch (error) {
      handleError(error);
    }
  };

  return (
    <Layout className={styles.layout}>
      <Block className={styles["form-container"]}>
        <form onSubmit={handleAuth}>
          <h1 className={styles["auth-form__title"]}>
            {hasAccount ? "Login" : "Register"}
          </h1>
          <br />
          <Username
            username={username}
            setUsername={(newUsername) => setUsername(newUsername)}
          />
          {!hasAccount && (
            <Email
              email={email}
              setEmail={(newEmail: string) => setEmail(newEmail)}
            />
          )}
          <Password
            password={password}
            setPassword={(newPassword: string) => setPassword(newPassword)}
          />
          <br />
          <Button
            className={styles["submit-button"]}
            type="submit"
            label="Submit"
            variant={"accent"}
          />

          <br />
          <p className={styles["error-text"]}>{errMsg}</p>
          <p>
            {hasAccount
              ? "Don't have an account yet?"
              : "Already have an account?"}
          </p>
          <button
            type="button"
            onClick={() => setHasAccount(!hasAccount)}
            className={styles["login-register-link"]}
          >
            {hasAccount ? "Register" : "Login"}
          </button>
        </form>
      </Block>
    </Layout>
  );
};
