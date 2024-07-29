import { ReactNode } from "react";
import { Button } from "../Button/Button";
import styles from "./Panel.module.css";

export const Panel = ({
  className,
  children,
  handleToggleClosed,
  closed, // Assuming this is a new prop indicating the panel state
}: {
  className?: string;
  children: ReactNode;
  handleToggleClosed: () => void;
  closed: boolean; // New prop added here
}) => (
  <div className={`${styles.panel} ${className}`}>
    <div
      className={styles[closed ? "panel__header--collapsed" : "panel__header"]}
    >
      <Button
        className={styles["panel__toggle-button"]}
        onClick={handleToggleClosed}
      >
        <img
          alt={closed ? ">" : "<"}
          src={closed ? "left_panel_open.svg" : "left_panel_close.svg"}
        />
      </Button>
    </div>
    {children}
  </div>
);
