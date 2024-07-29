import { useState } from "react";
import { Button } from "../../../../../shared/components/Button/Button";
import styles from "./SearchButton.module.css";

export const SearchButton = ({
  searchValueEmpty,
  onClick,
}: {
  searchValueEmpty: boolean;
  onClick: () => void;
}) => {
  const [isSpinning, setIsSpinning] = useState(false);

  const handleClick = () => {
    if (isSpinning) return;

    setIsSpinning(true);
    onClick();
    setTimeout(() => {
      setIsSpinning(false);
    }, 1000); // Duration should match the animation duration in CSS
  };

  return (
    <Button className={styles["search-button"]} onClick={handleClick}>
      <img
        alt={searchValueEmpty ? "update" : "search"}
        src={searchValueEmpty ? "/sync.svg" : "/search.svg"}
        className={searchValueEmpty && isSpinning ? styles.spin : ""}
      />
    </Button>
  );
};
