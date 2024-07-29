import { Button } from "../Button/Button";
import styles from "./PageSelector.module.css";

interface PageSelectorProps {
  totalPages: number;
  page: number;
  setPage: (page: number) => void;
}

export const PageSelector = ({
  totalPages,
  page,
  setPage,
}: PageSelectorProps) => {
  const handleClick = (pageNum: number) => {
    if (pageNum < 0 || pageNum >= totalPages) {
      return;
    }
    setPage(pageNum);
  };

  return (
    <div className={styles["page-selector"]}>
      <Button
        iconPath="/back.svg"
        className={
          page === 0
            ? `${styles["page-selector__button"]} ${styles["page-selector__direction-button--inactive"]}`
            : styles["page-selector__button"]
        }
        onClick={() => handleClick(page - 1)}
      />
      {Array.from({ length: totalPages }, (_, index) => (
        <Button
          label={String(index + 1)}
          key={index}
          className={
            index === page
              ? `${styles["page-selector__button"]} ${styles["page-selector__button--active"]}`
              : styles["page-selector__button"]
          }
          onClick={() => handleClick(index)}
        />
      ))}
      <Button
        iconPath="/forward.svg"
        className={
          page === totalPages - 1
            ? `${styles["page-selector__button"]} ${styles["page-selector__direction-button--inactive"]}`
            : styles["page-selector__button"]
        }
        onClick={() => handleClick(page + 1)}
      />
    </div>
  );
};
