import { useState } from "react";
import { NoteEditorContainer } from "../../features/notes/components/NoteEditor/NoteEditorContainer";
import { NotesListContainer } from "../../features/notes/components/NotesList/NotesListContainer";

import { SearchContainer } from "../../features/search/components/SearchContainer";
import { UserProfileContainer } from "../../features/user/components/UserProfile/UserProfileContainer";
import { Layout } from "../../shared/components/Layout/Layout";
import { Panel } from "../../shared/components/Panel/Panel";
import { PanelContent } from "../../shared/components/Panel/PanelContent/PanelContent";

export const Dashboard = () => {
  const [panelClosed, setPanelClosed] = useState(false);

  return (
    <Layout>
      <Panel
        closed={panelClosed}
        handleToggleClosed={() => setPanelClosed(!panelClosed)}
      >
        <PanelContent closed={panelClosed}>
          <SearchContainer />
          <NotesListContainer />
        </PanelContent>
      </Panel>
      <NoteEditorContainer headerChildren={<UserProfileContainer />} />
    </Layout>
  );
};
