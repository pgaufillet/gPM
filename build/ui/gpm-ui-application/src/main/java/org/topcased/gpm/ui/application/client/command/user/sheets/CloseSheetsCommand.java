package org.topcased.gpm.ui.application.client.command.user.sheets;

import java.util.ArrayList;
import java.util.List;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.command.CloseTabAction;
import org.topcased.gpm.ui.application.client.common.container.sheet.SheetPresenter;
import org.topcased.gpm.ui.application.client.event.LocalEvent;
import org.topcased.gpm.ui.application.client.user.ProductWorkspacePresenter;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

public class CloseSheetsCommand extends AbstractCommand implements Command {
    /**
     * Create an CloseSheetsCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public CloseSheetsCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.Command#execute()
     */
    public void execute() {

        final ProductWorkspacePresenter lCurrentProductPresenter =
                Application.INJECTOR.getUserSpacePresenter().getCurrentProductWorkspace();

        final List<String> lOpenedSheetIds =
                new ArrayList<String>(
                        lCurrentProductPresenter.getDetail().getPresenterIds());

        if (!lOpenedSheetIds.isEmpty()) {
            // get the list of modified sheet references
            List<String> lModifiedSheetRefs = new ArrayList<String>();
            for (String lId : lOpenedSheetIds) {
                SheetPresenter lSheetPresenter =
                        (SheetPresenter) lCurrentProductPresenter.getDetail().getPresenterById(
                                lId);
                if (lSheetPresenter != null
                        && lSheetPresenter.getUpdatedFields().size() != 0
                        || lSheetPresenter.getUpdatedLinkFields().size() != 0) {
                    lModifiedSheetRefs.add(lSheetPresenter.getDisplay().getTabTitle());
                }
            }

            String lConfirmationMessage = null;
            if (!lModifiedSheetRefs.isEmpty()) {
                StringBuffer lSheetList = new StringBuffer();
                for (String lSheetRef : lModifiedSheetRefs) {
                    if (lSheetList.length() > 0) {
                        lSheetList.append(", ");
                    }
                    lSheetList.append(lSheetRef);
                }
                lConfirmationMessage =
                        Ui18n.MESSAGES.confirmationCloseSheetsModified(lSheetList.toString());
            }
            else {
                lConfirmationMessage =
                        Ui18n.MESSAGES.confirmationCloseSheetsNotModified();
            }
            
            Application.INJECTOR.getConfirmationMessagePresenter().displayQuestion(lConfirmationMessage,
                    new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            for (String lSheetId : lOpenedSheetIds) {
                                fireEvent(
                                        LocalEvent.CLOSE_SHEET.getType(getCurrentProductWorkspaceName()),
                                        new CloseTabAction(lSheetId, true));
                            }
                        }
                    });
        }
    }

}
